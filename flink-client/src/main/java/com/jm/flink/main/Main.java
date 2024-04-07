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
package com.jm.flink.main;


import com.jm.flink.base.bean.EnvParams;
import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.base.conf.MetricPluginConf;
import com.jm.flink.base.conf.SyncConf;
import com.jm.flink.base.conf.SyncJobConf;
import com.jm.flink.base.enums.JobType;
import com.jm.flink.base.exception.FlinkDoRuntimeException;
import com.jm.flink.base.utils.*;
import com.jm.flink.environment.FlinkDoLocalStreamEnvironment;
import com.jm.flink.environment.helper.EnvPluginHelper;
import com.jm.flink.sql.parse.SqlParserHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.core.execution.JobClient;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.TableException;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * The FlinkRun class entry
 *
 * @author jinmu
 * @created 2022/4/9
 */
public class Main {

    public static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        log.info("------------FlinkRun start-------------------------");

        Arrays.stream(args).forEach(arg -> log.debug("job param: {}", arg));

        JobOptions jobOptions = new JobOptionParserUtil(args).getJobOptions();
        if (null == jobOptions) {
            log.error("FlinkRun job error: jobOptions is Required!");
            return;
        }
        String jobContent = StringUtil.toURLDecode(jobOptions.getJobContent());

        if (StringUtils.isBlank(jobContent)) {
            log.error("FlinkRun job error: jobContent is blank !");
            return;
        }
        jobOptions.setJobContent(jobContent);

        log.info("StreamExecutionEnvironment Init");
        StreamExecutionEnvironment env =
                EnvPluginHelper.createStreamExecutionEnvironment(jobOptions);

        log.info("StreamTableEnvironment Init");
        StreamTableEnvironment streamTableEnvironment =
                EnvPluginHelper.createStreamTableEnvironment(env, jobOptions);

        switch (JobType.getByName(jobOptions.getJobType())) {
            case SQL:
                log.info("start exeSqlJob");
                exeSqlJob(env, streamTableEnvironment, jobOptions);
                break;
            case STREAM:
                log.info("start exeStreamJob");
                exeStreamJob(env, streamTableEnvironment, jobOptions);
                break;
            default:
                log.error(
                        "unknown jobType: ["
                                + jobOptions.getJobType()
                                + "], jobType must in [SQL, STREAM].");
                throw new FlinkDoRuntimeException(
                        "unknown jobType: ["
                                + jobOptions.getJobType()
                                + "], jobType must in [SQL, STREAM].");
        }

        log.info("------------FlinkRun end-------------------------");
    }

    /**
     * ，本地方法运行本地模式
     *
     * @param jobOptions
     * @return java.util.Optional<org.apache.flink.core.execution.JobClient>
     * @author jinmu
     * @created 2022/4/29
     */
    @SuppressWarnings("AlibabaSwitchStatement")
    public static Optional<JobClient> localNativeJob(JobOptions jobOptions) throws Exception {

        log.info("------------FlinkRun start-------------------------");

        if (null == jobOptions) {
            log.error("FlinkRun job error: jobOptions is Required!");
            return Optional.empty();
        }
        String jobContent = StringUtil.toURLDecode(jobOptions.getJobContent());

        if (StringUtils.isBlank(jobContent)) {
            log.error("FlinkRun job error: jobContent is blank !");
            return Optional.empty();
        }
        log.info("StreamExecutionEnvironment Init");
        StreamExecutionEnvironment env =
                EnvPluginHelper.createStreamExecutionEnvironment(jobOptions);

        log.info("StreamTableEnvironment Init");
        StreamTableEnvironment streamTableEnvironment =
                EnvPluginHelper.createStreamTableEnvironment(env, jobOptions);
        switch (JobType.getByName(jobOptions.getJobType())) {
            case SQL:
                log.info("start exeSqlJob");
                return exeSqlJob(env, streamTableEnvironment, jobOptions);
            case STREAM:
                log.info("start exeStreamJob");
                exeStreamJob(env, streamTableEnvironment, jobOptions);
                break;
            default:
                log.error(
                        "unknown jobType: ["
                                + jobOptions.getJobType()
                                + "], jobType must in [SQL, STREAM].");
                throw new FlinkDoRuntimeException(
                        "unknown jobType: ["
                                + jobOptions.getJobType()
                                + "], jobType must in [SQL, STREAM].");
        }

        log.info("------------FlinkRun end-------------------------");
        return Optional.empty();
    }

    /**
     * 执行sql类型任务
     *
     * <p>返回Optional<JobClient>为了解决，本地方法运行本地模式，可以停止任务
     *
     * @param jobOptions
     * @return void
     * @author jinmu
     * @param: env
     * @param: tableEnv
     * @created 2022/4/11
     */
    @SuppressWarnings({"AlibabaAvoidManuallyCreateThread", "DuplicatedCode"})
    private static Optional<JobClient> exeSqlJob(
            StreamExecutionEnvironment streamExecutionEnvironment,
            StreamTableEnvironment streamTableEnvironment,
            JobOptions jobOptions)
            throws InterruptedException {
        try {
            EnvParams envParams = FlinkEnvUtils.loadEnvParams(jobOptions);

            Set<URL> jarUrlList = PluginUtil.getShipJars(envParams.getShipJars());
            PluginUtil.registerPluginUrlToCachedFile(streamExecutionEnvironment, jarUrlList);
            log.debug(
                    "加载ShipJars成功,数量{},列表:{}", jarUrlList.size(), JsonUtil.toJson(jarUrlList));

            Set<URL> userJarLib = PluginUtil.getUserJarsByLib(envParams.getUserJarLib());
            PluginUtil.registerPluginUrlToCachedFile(streamExecutionEnvironment, userJarLib);
            log.debug(
                    "加载userJarLib成功,数量{},列表:{}",
                    userJarLib.size(),
                    JsonUtil.toJson(userJarLib));

            EnvPluginHelper.configSqlStreamExecutionEnvironment(
                    streamExecutionEnvironment, envParams, jobOptions.getMode());

            // sql statement set to exec
            StatementSet statementSet =
                    SqlParserHelper.parseSql(
                            jobOptions.getJobContent(),
                            new ArrayList<>(jarUrlList), null,
                            streamTableEnvironment);
            TableResult execute = statementSet.execute();
            if (streamExecutionEnvironment instanceof FlinkDoLocalStreamEnvironment) {
                Optional<JobClient> jobClient = execute.getJobClient();
                jobClient.ifPresent(client -> new Thread(() -> {
                    try {
                        JobResultPrintUtil.printSqlResult(client.getAccumulators().get());
                    } catch (InterruptedException | ExecutionException exception) {
                        log.error("JobResultPrintUtil printSqlResult error:{}", exception.getMessage());
                    }
                }));
                return jobClient;
            }
        } catch (Exception e) {
            // 明确告知线程是被中断的(可能外部测试当前线程引发)
            if (e instanceof TableException && e.getCause() instanceof InterruptedException) {
                log.error("------------FlinkRun  program is Interrupted-------------------------");
            }
            throw new FlinkDoRuntimeException(e);
        } finally {
            // 清理ThreadLocal
//            FactoryUtil.getFactoryHelperThreadLocal().remove();
//            TableFactoryService.getFactoryHelperThreadLocal().remove();
            log.debug("exeSqlJob done,jobOptions:{}", JsonUtil.toJson(jobOptions));
        }
        return Optional.empty();
    }

    /**
     * 执行数据同步类型任务
     *
     * @param jobOptions
     * @return void
     * @author jinmu
     * @param: env
     * @param: tableEnv
     * @created 2022/4/11
     */
    private static void exeStreamJob(
            StreamExecutionEnvironment streamExecutionEnvironment,
            StreamTableEnvironment tableEnv,
            JobOptions jobOptions)
            throws Exception {

        // 将任务所用到的其他插件包注册到env中(Job依赖的插件包路径存储到cacheFile，在外围将插件包路径传递给jobGraph)
        EnvParams envParams = FlinkEnvUtils.loadEnvParams(jobOptions);

        Set<URL> jarUrlList = PluginUtil.getShipJars(envParams.getShipJars());
        PluginUtil.registerPluginUrlToCachedFile(streamExecutionEnvironment, jarUrlList);
        log.info("加载ShipJars成功,数量{},列表:{}", jarUrlList.size(), JsonUtil.toJson(jarUrlList));

        Set<URL> userJarLib = PluginUtil.getUserJarsByLib(envParams.getUserJarLib());
        PluginUtil.registerPluginUrlToCachedFile(streamExecutionEnvironment, userJarLib);
        log.info("加载userJarLib成功,数量{},列表:{}", userJarLib.size(), JsonUtil.toJson(userJarLib));

        // 同步配置
        SyncConf syncConf = new SyncConf();

        // 同步任务配置
        SyncJobConf syncJobConf = new SyncJobConf();

        // 监控配置
        MetricPluginConf metricPluginConf = new MetricPluginConf();

        // 将任务用到插件注册到缓存中
        PluginUtil.registerJobPluginToCachedFile(
                streamExecutionEnvironment, envParams, syncJobConf, metricPluginConf);


//        // todo business codes
//        SourceFactory sourceFactory =
//                DataSyncFactoryUtil.discoverSource(syncConf, streamExecutionEnvironment);
//        DataStream<RowData> dataStreamSource = sourceFactory.createSource();
    }
}
