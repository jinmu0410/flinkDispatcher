/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jm.flink.helper;


import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.base.bean.JobParams;
import com.jm.flink.base.constants.ConfigConstant;
import com.jm.flink.base.utils.GsonUtil;
import com.jm.flink.base.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterRetrieveException;
import org.apache.flink.client.deployment.DefaultClusterClientServiceLoader;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptionsInternal;
import org.apache.flink.runtime.messages.Acknowledge;
import org.apache.flink.util.FlinkException;
import org.apache.flink.yarn.Utils;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnLogConfigUtil;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.apache.flink.util.Preconditions.checkNotNull;

/**
 * flink on yarn helper
 *
 * @author tasher
 * @created 2022/4/11
 * @return
 */
public class YarnClientHelper {

    private static final Logger logger = LoggerFactory.getLogger(YarnClientHelper.class);

    /**
     * 创建YarnClient
     *
     * @param yarnConfiguration
     * @return org.apache.hadoop.yarn.client.api.YarnClient
     * @author tasher
     * @created 2022/4/11
     */
    public static YarnClient createSimpleYarnClient(YarnConfiguration yarnConfiguration) {
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(yarnConfiguration);
        return yarnClient;
    }

    /**
     * buildApplicationInstance
     *
     * @param applicationIdStr
     * @return org.apache.hadoop.yarn.api.records.ApplicationId
     * @author tasher
     * @created 2022/4/11
     */
    public static ApplicationId buildApplicationInstance(String applicationIdStr) {
        if (StringUtils.isBlank(applicationIdStr)
                || !applicationIdStr.startsWith(ApplicationId.appIdStrPrefix)
                || !applicationIdStr.contains("_")) {
            logger.error("not support instance applicationIdStr");
            return null;
        }
        return ConverterUtils.toApplicationId(applicationIdStr);
    }

    public static ApplicationId getPeekApplicationId(
            YarnClient yarnClient, Configuration flinkConfig) throws Exception {
        Set<String> set = new HashSet<>();
        // applicationTypes
        set.add("Apache Flink");
        EnumSet<YarnApplicationState> enumSet = EnumSet.noneOf(YarnApplicationState.class);
        enumSet.add(YarnApplicationState.RUNNING);
        List<ApplicationReport> reportList = yarnClient.getApplications(set, enumSet);
        ApplicationId applicationId = null;
        int maxMemory = -1;
        int maxCores = -1;
        for (ApplicationReport report : reportList) {
            if (!report.getYarnApplicationState().equals(YarnApplicationState.RUNNING)) {
                logger.info(
                        "applicationName:{},applicationID :{}, not running",
                        report.getName(),
                        report.getApplicationId());
                continue;
            }

            // 队列匹配
            String queue =
                    null != flinkConfig
                            ? flinkConfig.get(YarnConfigOptions.APPLICATION_QUEUE)
                            : "default";
            if (!report.getQueue().equals(queue)) {
                logger.info(
                        "applicationName:{},applicationID :{}, not in queue:{}",
                        report.getName(),
                        report.getApplicationId(),
                        queue);
                continue;
            }

            // 计算资源匹配
            int thisMemory =
                    report.getApplicationResourceUsageReport().getNeededResources().getMemory();
            int thisCores =
                    report.getApplicationResourceUsageReport()
                            .getNeededResources()
                            .getVirtualCores();

            boolean isOverMaxResource =
                    thisMemory > maxMemory || thisMemory == maxMemory && thisCores > maxCores;
            if (isOverMaxResource) {
                maxMemory = thisMemory;
                maxCores = thisCores;
                applicationId = report.getApplicationId();
            }
        }

        return applicationId;
    }

    /**
     * 清除保存的状态快照
     *
     * @param jobOptions
     * @return java.lang.Boolean
     * @author tasher
     * @created 2022/3/1
     */
    public static Boolean disposeFlinkJobSavePoint(JobOptions jobOptions) {

        if (null == jobOptions) {
            logger.error("jobOptions can't be empty");
            return Boolean.FALSE;
        }

        if (StringUtils.isBlank(jobOptions.getJobParams())) {
            logger.error("jobId can't be empty");
            return Boolean.FALSE;
        }

        JobParams jobParams = JsonUtil.toObject(jobOptions.getJobParams(), JobParams.class);
        if (null == jobParams
                || StringUtils.isBlank(jobParams.getYarnApplicationId())
                || StringUtils.isBlank(jobParams.getFlinkJobId())) {
            logger.error(
                    "jobParams or  yarnApplicationId or flinkJobId may be empty,jobparams:{}",
                    jobOptions.getJobParams());
            return Boolean.FALSE;
        }
        Configuration flinkConfiguration = FlinkEnvHelper.loadFlinkConfiguration(jobOptions);
        String savePointPath = flinkConfiguration.getString(ConfigConstant.SAVE_POINT_PATH_KEY, "");
        if (StringUtils.isBlank(savePointPath)) {
            logger.error("savePointPath is empty,jobparams:{}", jobOptions.getJobParams());
            return Boolean.FALSE;
        }
        YarnConfiguration yarnConfiguration = FlinkEnvHelper.loadHadoopConfig(jobOptions);
        YarnClient yarnClient = YarnClientHelper.createSimpleYarnClient(yarnConfiguration);

        try {
            YarnClusterDescriptor yarnClusterDescriptor =
                    new YarnClusterDescriptor(
                            flinkConfiguration,
                            yarnConfiguration,
                            yarnClient,
                            YarnClientYarnClusterInformationRetriever.create(yarnClient),
                            true);
            JobID jobID = JobID.fromHexString(jobParams.getFlinkJobId());
            ClusterClient<ApplicationId> clusterClient =
                    yarnClusterDescriptor
                            .retrieve(
                                    YarnClientHelper.buildApplicationInstance(
                                            jobParams.getYarnApplicationId()))
                            .getClusterClient();
            logger.info(
                    "clusterClient retrieve success,clusterId:{}", clusterClient.getClusterId());
            try {
                Acknowledge acknowledge = clusterClient.disposeSavepoint(savePointPath).get();
                if (null != acknowledge) {
                    return Boolean.TRUE;
                }
            } catch (InterruptedException | ExecutionException | FlinkException e) {
                logger.error("clusterClient cancel error, please try again,{}", e.getMessage(), e);
                return Boolean.FALSE;
            }

        } catch (ClusterRetrieveException | RuntimeException e) {
            logger.error(
                    "applicationId:{},jobId:{},cancel error,clusterClient retrieve error:{}",
                    jobParams.getYarnApplicationId(),
                    jobParams.getFlinkJobId(),
                    e.getMessage());
            return Boolean.FALSE;
        } finally {
            yarnClient.stop();
        }
        return Boolean.FALSE;
    }

    public YarnClusterDescriptor createClusterDescriptor(Configuration configuration) {
        checkNotNull(configuration);

        final String configurationDirectory = configuration.get(DeploymentOptionsInternal.CONF_DIR);
        YarnLogConfigUtil.setLogConfigFileInConfig(configuration, configurationDirectory);

        final YarnClient yarnClient = YarnClient.createYarnClient();
        final YarnConfiguration yarnConfiguration =
                Utils.getYarnAndHadoopConfiguration(configuration);

        yarnClient.init(yarnConfiguration);
        yarnClient.start();

        return new YarnClusterDescriptor(
                configuration,
                yarnConfiguration,
                yarnClient,
                YarnClientYarnClusterInformationRetriever.create(yarnClient),
                false);
    }

}
