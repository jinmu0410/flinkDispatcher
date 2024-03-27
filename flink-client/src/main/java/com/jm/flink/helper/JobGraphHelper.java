/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import com.jm.flink.base.constants.ConfigConstant;
import com.jm.flink.base.enums.PluginMode;
import com.jm.flink.base.exception.FlinkDoRuntimeException;
import com.jm.flink.base.utils.JobOptionParserUtil;
import com.jm.flink.constant.FlinkDoConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.flink.configuration.CoreOptions.DEFAULT_PARALLELISM;

/**
 * Flink作业构建帮助类
 *
 * @author tasher
 * @created 2022/2/21
 * @return
 */
public class JobGraphHelper {

    private static final Logger logger = LoggerFactory.getLogger(JobGraphHelper.class);

    /**
     * 构建Flink作业
     *
     * @param jobOptions
     * @return org.apache.flink.runtime.jobgraph.JobGraph
     * @author tasher
     * @created 2022/4/13
     */
    public static JobGraph buildJobGraph(JobOptions jobOptions) throws Exception {

        // option => String[] args
        List<String> execArgs = JobOptionParserUtil.transformOptionsArgs(jobOptions);

        // core dist jar
        String coreMainJar = jobOptions.getJobMainClassJar();
        if (StringUtils.isBlank(coreMainJar)) {
            throw new FlinkDoRuntimeException("required main class miss!");
        }

        // 加载flinkdo核心JAR包
        File jarFile = new File(coreMainJar);
        Configuration flinkConfiguration = FlinkEnvHelper.loadFlinkConfiguration(jobOptions);

        // 创建无作业参数配置program
        PackagedProgram program =
                PackagedProgram.newBuilder()
                        .setJarFile(jarFile)
                        .setEntryPointClassName(FlinkDoConstants.MAIN_CLASS)
                        .setConfiguration(flinkConfiguration)
                        .setArguments(execArgs.toArray(new String[0]))
                        .build();

        // 构建flink 作业
        JobGraph jobGraph =
                PackagedProgramUtils.createJobGraph(
                        program,
                        flinkConfiguration,
                        flinkConfiguration.getInteger(DEFAULT_PARALLELISM, 1),
                        false);

//        // todo 这块不需要了
//        // 设置作业依赖所有插件列表(FlinkDo core中加载环境插件配置)
//        List<URL> pluginClassPath =
//                jobGraph.getUserArtifacts().entrySet().stream()
//                        .filter(
//                                tmp ->
//                                        tmp.getKey()
//                                                .startsWith(
//                                                        FlinkDoConstants
//                                                                .CLASS_FILE_NAME_FMT_PREFIX))
//                        .map(tmp -> new File(tmp.getValue().filePath))
//                        .map(
//                                file -> {
//                                    try {
//                                        return file.toURI().toURL();
//                                    } catch (MalformedURLException e) {
//                                        logger.error(e.getMessage());
//                                    }
//                                    return null;
//                                })
//                        .collect(Collectors.toList());
//        jobGraph.setClasspaths(pluginClassPath);

//        EnvParams envParams = FlinkEnvHelper.loadEnvParams(jobOptions);
//        if (null != envParams) {
//            if (PluginMode.SHIPFILE.getName().equals(envParams.getPluginMode())) {
//                // 执行任务上传资源jar
//                jobGraph.addJars(pluginClassPath);
//            }
//        }

        // 设置从savepoint启动(单独提交jobGraph需要设置)
        if (flinkConfiguration.containsKey(ConfigConstant.EXCUTION_SAVEPOINT_PATH)) {
            String savePointPath = flinkConfiguration.getString(ConfigOptions.key(ConfigConstant.EXCUTION_SAVEPOINT_PATH).stringType().defaultValue(""));
            if (StringUtils.isNotBlank(savePointPath)) {
                jobGraph.setSavepointRestoreSettings(
                        SavepointRestoreSettings.forPath(savePointPath));
            }
        }
        return jobGraph;
    }
}
