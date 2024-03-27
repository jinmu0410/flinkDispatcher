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


import com.jm.flink.base.bean.EnvParams;
import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.base.constants.FlinkDoConstants;
import com.jm.flink.base.enums.PluginMode;
import com.jm.flink.base.utils.JsonUtil;
import com.jm.flink.base.utils.PropertiesUtil;
import com.jm.flink.base.utils.YarnConfLoaderUtil;
import com.jm.flink.constant.FlinkDoPluginConstants;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.configuration.*;
import org.apache.flink.configuration.description.Description;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnConfigOptionsInternal;
import org.apache.flink.yarn.configuration.YarnDeploymentTarget;
import org.apache.flink.yarn.configuration.YarnLogConfigUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static com.jm.flink.base.enums.DeployMode.*;
import static com.jm.flink.constant.FlinkDoConstants.FLINK_VERSION_DIST_JAR;

/**
 * flink env helper
 *
 * <p>=== 对于简单进行总内存管理，采用以下配置之一 指定TaskManager进程的总内存: taskmanager.memory.process.size 或 指定Flink总内存:
 * taskmanager.memory.flink.size
 *
 * <p>=== 对于生产环境服务环境明确采用这种方式 指定JVM堆内存大小: taskmanager.memory.task.heap.size: 2048m 指定JVM托管内存大小:
 * taskmanager.memory.managed.size: 512m
 *
 * @author tasher
 * @created 2022/4/11
 * @return
 */
public class FlinkEnvHelper {

    private static final Logger logger = LoggerFactory.getLogger(FlinkEnvHelper.class);

    /**
     * 默认YARN 用户
     */
    private static final String DEFAULT_DEPLOY_USER = "hadoop";

    /**
     * 默认flink on yarn application name
     */
    public static final String DEFAULT_APPLICATION_SESSION_NAME =
            "FlinkDO-Flink-Job Session Cluster";

    public static final ConfigOption<Integer> FLINK_JOB_MPSB =
            ConfigOptions.key("jobmanager.memory.process.size")
                    .intType()
                    .defaultValue(1024)
                    .withDescription(
                            Description.builder().text("JOBMANAGER_MEMORY_MB BY PROCESS").build());

    public static final ConfigOption<Integer> FLINK_TASK_MMB =
            ConfigOptions.key("taskmanager.memory.process.size")
                    .intType()
                    .defaultValue(1024)
                    .withDescription(
                            Description.builder().text("TASKMANAGER_MEMORY_MB BY PROCESS").build());

    public static final ConfigOption<Integer> FLINK_SLOTS_PER =
            ConfigOptions.key("taskmanager.slots")
                    .intType()
                    .defaultValue(1)
                    .withDescription(Description.builder().text("slotsPerTaskManager").build());

    /**
     * 加载环境参数
     *
     * @param jobOptions
     * @return com.hk.szyc.flinkdo.core.base.bean.EnvParams
     * @author tasher
     * @created 2022/4/15
     */
    public static EnvParams loadEnvParams(JobOptions jobOptions) {
        if (null == jobOptions || StringUtils.isBlank(jobOptions.getEnvParams())) {
            return new EnvParams();
        }
        return JsonUtil.toObject(jobOptions.getEnvParams(), EnvParams.class);
    }

    /**
     * 加载hadoopP诶之
     *
     * @param jobOptions
     * @return org.apache.hadoop.yarn.conf.YarnConfiguration
     * @author tasher
     * @created 2022/4/15
     */
    public static YarnConfiguration loadHadoopConfig(JobOptions jobOptions) {

        if (null == jobOptions || StringUtils.isBlank(jobOptions.getEnvParams())) {
            return null;
        }
        EnvParams envParams = JsonUtil.toObject(jobOptions.getEnvParams(), EnvParams.class);
        if (null == envParams || StringUtils.isBlank(envParams.getHadoopConfDir())) {
            return null;
        }
        YarnConfiguration yarnConfiguration =
                YarnConfLoaderUtil.loadYarnConfInDir(envParams.getHadoopConfDir());
        if (StringUtils.isNotBlank(envParams.getHadoopUser())) {
            yarnConfiguration.set("HADOOP.USER.NAME", envParams.getHadoopUser());
        } else {
            yarnConfiguration.set("HADOOP.USER.NAME", DEFAULT_DEPLOY_USER);
        }
        if (StringUtils.isNotBlank(envParams.getYarnQueue())) {
            yarnConfiguration.set("queue", envParams.getYarnQueue());
        } else {
            yarnConfiguration.set("queue", "default");
        }
        return yarnConfiguration;
    }

    /**
     * 初始化FLINK 配置
     *
     * @param jobOptions
     * @return org.apache.flink.configuration.Configuration
     * @author tasher
     * @created 2022/4/11
     */
    @SuppressWarnings("unchecked")
    public static Configuration loadFlinkConfiguration(JobOptions jobOptions) {
        Configuration flinkConfiguration = new Configuration();

        EnvParams envParams = loadEnvParams(jobOptions);
        if (jobOptions.getMode().equals(yarnSession.getName())) {
            flinkConfiguration.set(
                    DeploymentOptions.TARGET,
                    YarnDeploymentTarget.SESSION.getName());
        } else if (jobOptions.getMode().equals(yarnApplication.getName())) {
            flinkConfiguration.set(
                    DeploymentOptions.TARGET,
                    YarnDeploymentTarget.APPLICATION.getName());

            flinkConfiguration.set(
                    PipelineOptions.JARS,
                    Collections.singletonList(jobOptions.getJobMainClassJar()));

            Path remoteLib = new Path(envParams.getFlinkLibDir());
            flinkConfiguration.set(
                    YarnConfigOptions.PROVIDED_LIB_DIRS,
                    Collections.singletonList(remoteLib.toString()));

            flinkConfiguration.set(
                    YarnConfigOptions.FLINK_DIST_JAR,
                    envParams.getFlinkLibDir() + FLINK_VERSION_DIST_JAR);
        }else if(jobOptions.getMode().equals(standalone.getName())){
            flinkConfiguration.setString(RestOptions.ADDRESS, envParams.getJobManagerRpcAddress());
        }

        if (StringUtils.isNotEmpty(envParams.getFlinkConfDir())) {
            // flink配置路径
            if (envParams.getFlinkConfDir().startsWith(FlinkDoConstants.FILE_PATH_SPLIT)) {
                // 本地路径下的文件才加载FLINK 配置
                flinkConfiguration =
                        GlobalConfiguration.loadConfiguration(envParams.getFlinkConfDir());
            }
        }
        // 自定义动态flink参数
        if (StringUtils.isNotBlank(envParams.getFlinkDynamicConf())) {
            Configuration dynamicConf =
                    Configuration.fromMap(
                            PropertiesUtil.confToMap(envParams.getFlinkDynamicConf()));
            flinkConfiguration.addAll(dynamicConf);
        }
        if (StringUtils.isNotBlank(jobOptions.getJobName())) {
            flinkConfiguration.setString(
                    YarnConfigOptions.APPLICATION_NAME, jobOptions.getJobName());
        }
        if (StringUtils.isNotBlank(envParams.getHadoopConfDir())) {
            flinkConfiguration.setString(
                    ConfigConstants.PATH_HADOOP_CONFIG, envParams.getHadoopConfDir());
        }
        if (StringUtils.isNotBlank(envParams.getYarnQueue())) {
            flinkConfiguration.setString(
                    YarnConfigOptions.APPLICATION_QUEUE, envParams.getYarnQueue());
        }
        if (StringUtils.isNotBlank(envParams.getFlinkLibDir())
                && envParams.getFlinkLibDir().startsWith(FlinkDoConstants.FILE_PATH_HDFS)) {
            // 代理作业flink目录
            flinkConfiguration.set(
                    YarnConfigOptions.PROVIDED_LIB_DIRS,
                    Collections.singletonList(envParams.getFlinkLibDir()));
        }

        // class_loader 模式
        if (PluginMode.CLASSPATH.getName().equalsIgnoreCase(envParams.getPluginMode())) {
            flinkConfiguration.setString(CoreOptions.CLASSLOADER_RESOLVE_ORDER, "child-first");
        } else {
            flinkConfiguration.setString(CoreOptions.CLASSLOADER_RESOLVE_ORDER, "parent-first");
        }

        // classloader check
        flinkConfiguration.set(CoreOptions.CHECK_LEAKED_CLASSLOADER, false);


        // 配置HOST映射
        if (StringUtils.isNotBlank(envParams.getYarnHosts())) {
            TreeMap<String, String> hostMapping =
                    JsonUtil.toObject(envParams.getYarnHosts(), TreeMap.class);
            if (null != hostMapping) {
                for (String hostKey : hostMapping.keySet()) {
                    flinkConfiguration.setString(
                            FlinkDoPluginConstants.KEY_HOST_PREX + hostKey,
                            hostMapping.get(hostKey));
                }
            }
        }
        // 配置日志
        configFlinkLog(flinkConfiguration, envParams);
        return flinkConfiguration;
    }

    /**
     * Flink 日志配置
     *
     * @param envParams
     * @return void
     * @author tasher
     * @param: configuration
     * @created 2022/4/11
     */
    public static void configFlinkLog(Configuration configuration, EnvParams envParams) {

        // 日志处理
        if (null == envParams || StringUtils.isBlank(envParams.getFlinkConfDir())) {
            logger.warn("config log4j error flinkLibPath is empty!");
            return;
        }

        if (!envParams.getFlinkConfDir().startsWith("/")) {
            logger.warn("not support remote path,{}", envParams.getFlinkConfDir());
            return;
        }
        File log4j =
                new File(
                        envParams.getFlinkConfDir()
                                + File.separator
                                + YarnLogConfigUtil.CONFIG_FILE_LOG4J_NAME);
        if (!log4j.exists()) {
            File logback =
                    new File(
                            envParams.getFlinkConfDir()
                                    + File.separator
                                    + YarnLogConfigUtil.CONFIG_FILE_LOGBACK_NAME);
            if (logback.exists()) {
                configuration.setString(
                        YarnConfigOptionsInternal.APPLICATION_LOG_CONFIG_FILE,
                        envParams.getFlinkConfDir()
                                + File.separator
                                + YarnLogConfigUtil.CONFIG_FILE_LOGBACK_NAME);
            }
        }
        configuration.setString(
                YarnConfigOptionsInternal.APPLICATION_LOG_CONFIG_FILE,
                envParams.getFlinkConfDir()
                        + File.separator
                        + YarnLogConfigUtil.CONFIG_FILE_LOG4J_NAME);
    }

    /**
     * flink集群空作业配置
     *
     * @param flinkConfiguration
     * @return org.apache.flink.client.deployment.ClusterSpecification
     * @author tasher
     * @created 2022/2/22
     */
    public static ClusterSpecification createClusterSpecification(
            Configuration flinkConfiguration) {
        ClusterSpecification buildClusterSpecification =
                new ClusterSpecification.ClusterSpecificationBuilder().createClusterSpecification();
        return buildClusterSpecification;
    }

    /**
     * createYarnClusterDescriptor
     *
     * <p>会加载FLINK配置环境 会加载Hadoop配置环境 会按需上传JAR
     *
     * @return org.apache.flink.yarn.YarnClusterDescriptor
     * @author tasher
     * @param: jobOptions
     * @created 2022/4/16
     */
    @SuppressWarnings("DuplicatedCode")
    public static YarnClusterDescriptor createYarnClusterDescriptor(JobOptions jobOptions)
            throws Exception {

        // flink配置
        Configuration flinkConfig = FlinkEnvHelper.loadFlinkConfiguration(jobOptions);

        // hadoop配置
        YarnConfiguration yarnConfiguration = FlinkEnvHelper.loadHadoopConfig(jobOptions);

        YarnClient yarnClient = YarnClientHelper.createSimpleYarnClient(yarnConfiguration);
        yarnClient.start();

        // yarn 作业配置
        YarnClusterDescriptor descriptor =
                new YarnClusterDescriptor(
                        flinkConfig,
                        yarnConfiguration,
                        yarnClient,
                        YarnClientYarnClusterInformationRetriever.create(yarnClient),
                        false);

        EnvParams envParams = FlinkEnvHelper.loadEnvParams(jobOptions);

        // flink jar 配置
        if (null != envParams
                && envParams.getFlinkLibDir().startsWith(FlinkDoConstants.FILE_PATH_HDFS)) {
            // 配置作业flink jar
            descriptor.setLocalJarPath(
                    new Path(envParams.getFlinkLibDir() + FlinkDoConstants.FLINK_VERSION_DIST_JAR));

        } else {
            if (null != envParams) {
                // 非HDFS目录，需要上传存在的jar
                String flinkPath = envParams.getFlinkLibDir();
                if (!envParams.getFlinkLibDir().contains("lib")) {
                    flinkPath = envParams.getFlinkLibDir() + "/lib";
                }
                File libDir = new File(flinkPath);
                if (libDir.isDirectory()) {
                    List<File> shipFiles = new ArrayList<>(1);
                    File[] jars = libDir.listFiles();
                    if (jars != null) {
                        for (File jar : jars) {
                            if (jar.toURI()
                                    .toURL()
                                    .toString()
                                    .contains(FlinkDoConstants.REGX_FLINK_DIST_NAME)) {
                                // 配置作业flink jar
                                descriptor.setLocalJarPath(
                                        new Path(jar.toURI().toURL().toString()));
                            } else {
                                // 上传jar
                                shipFiles.add(jar);
                            }
                        }
                    }
                    descriptor.addShipFiles(shipFiles);
                }
            }
        }

        // 是否上传任务参数中的JAR
        if (null != envParams && PluginMode.SHIPFILE.getName().equals(envParams.getPluginMode())) {
            List<File> shipFiles = Lists.newArrayList();
            if (StringUtils.isNotBlank(envParams.getUserJarLib())) {
                shipFiles.addAll(PluginHelper.getUserJarFilesByLib(envParams.getUserJarLib()));
            }
            if (StringUtils.isNotBlank(envParams.getShipJars())) {
                shipFiles.addAll(PluginHelper.getShipJarFiles(envParams.getShipJars()));
            }
            descriptor.addShipFiles(shipFiles);
        }
        return descriptor;
    }
}
