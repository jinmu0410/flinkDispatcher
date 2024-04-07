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

package com.jm.flink.environment.helper;


import com.jm.flink.base.bean.EnvParams;
import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.base.bean.JobParams;
import com.jm.flink.base.constants.ConfigConstant;
import com.jm.flink.base.enums.DeployMode;
import com.jm.flink.base.enums.PluginMode;
import com.jm.flink.base.exception.FlinkDoRuntimeException;
import com.jm.flink.base.utils.*;
import com.jm.flink.constant.FlinkDoConstants;
import com.jm.flink.environment.FlinkDoLocalStreamEnvironment;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableConfig;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.config.TableConfigOptions;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 环境加载
 *
 * @author jinmu
 * @created 2022/4/18
 */
public class EnvPluginHelper {

    private static final Logger LOG = LoggerFactory.getLogger(EnvPluginHelper.class);

    // TableEnvironment state ttl config
    private static final String TTL_PATTERN_STR = "^+?([1-9][0-9]*)([dDhHmMsS])$";
    private static final Pattern TTL_PATTERN = Pattern.compile(TTL_PATTERN_STR);

    /**
     * configSqlStreamExecutionEnvironment
     *
     * @param deployMode
     * @return void
     * @author jinmu
     * @param: env
     * @param: envParams
     * @created 2022/4/24
     */
    public static void configSqlStreamExecutionEnvironment(
            StreamExecutionEnvironment env, EnvParams envParams, String deployMode) {

        // // 将任务用到插件注册到缓存中,这里可以改造城解析flink sql
        // 非local模式或者shipfile部署模式，PluginPath必填
//        if (StringUtils.isBlank(envParams.getPluginPath())) {
//            if (StringUtils.equalsIgnoreCase(envParams.getPluginMode(), PluginMode.SHIPFILE.name())
//                    || StringUtils.equalsIgnoreCase(deployMode, DeployMode.local.name())) {
//                throw new FlinkDoRuntimeException(
//                        "Non-local mode or shipfile deployment mode, remoteSqlPluginPath is required");
//            }
//        }
//        // 加载source/sink 插件前先Load Plugin
//        FactoryHelper factoryHelper = new FactoryHelper();
//        factoryHelper.setPluginPath(envParams.getPluginPath());
//        factoryHelper.setPluginLoadMode(envParams.getPluginMode());
//        factoryHelper.setExecutionMode(deployMode);
//        factoryHelper.setEnv(env);
//
//        // 本地线程local
//        FactoryUtil.setFactoryUtilHelp(factoryHelper);
//        TableFactoryService.setFactoryUtilHelp(factoryHelper);
    }

    /**
     * 初始化FLINK 配置
     *
     * @param jobOptions
     * @return org.apache.flink.configuration.Configuration
     * @author jinmu
     * @created 2022/4/11
     */
    @SuppressWarnings("unchecked")
    public static Configuration initFlinkConfiguration(JobOptions jobOptions) {
        Configuration flinkConfiguration = new Configuration();

        EnvParams envParams = FlinkEnvUtils.loadEnvParams(jobOptions);

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
        // user role setting
        System.setProperty(
                "HADOOP_USER_NAME",
                Optional.ofNullable(envParams.getHadoopUser()).orElse("hadoop"));
        return flinkConfiguration;
    }

    /**
     * 创建StreamExecutionEnvironment
     *
     * @param jobOptions
     * @return org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
     * @author jinmu
     * @created 2022/4/10
     */
    public static StreamExecutionEnvironment createStreamExecutionEnvironment(JobOptions jobOptions)
            throws Exception {

        Configuration flinkConfiguration = initFlinkConfiguration(jobOptions);

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment(flinkConfiguration);
        env.getConfig().disableClosureCleaner();
        if (StringUtils.equalsIgnoreCase(DeployMode.local.getName(), jobOptions.getMode())) {
            return getLocalStreamEnvironment(flinkConfiguration);
        }

        //todo yarn-application模式jobParmas需要decode
        if (jobOptions.getMode().equals(DeployMode.yarnApplication.getName())) {
            jobOptions.setJobParams(StringUtil.toURLDecode(jobOptions.getJobParams()));
        }
        JobParams jobParams = JsonUtil.toObject(jobOptions.getJobParams(), JobParams.class);
        if (null != jobParams && null != jobParams.getSpeedConf()) {
            env.setParallelism(jobParams.getSpeedConf().getChannel());
        }

        // Flink 动态属性
        // 重启策略
        Optional<Boolean> isRestore = isRestore(flinkConfiguration);
        if (isRestore.isPresent() && isRestore.get()) {
            env.setRestartStrategy(
                    RestartStrategies.failureRateRestart(
                            ConfigConstant.FAILUEE_RATE,
                            Time.of(getFailureInterval(flinkConfiguration).get(), TimeUnit.MINUTES),
                            Time.of(getDelayInterval(flinkConfiguration).get(), TimeUnit.SECONDS)));
        } else {
            env.setRestartStrategy(RestartStrategies.noRestart());
        }
        // checkpoint setting(存在目录配置)
        if (flinkConfiguration.containsKey(ConfigConstant.CHECKPOINTS_DIRECTORY_KEY)) {

            String stateCheckDir = flinkConfiguration.getString(ConfigOptions.key(ConfigConstant.CHECKPOINTS_DIRECTORY_KEY).stringType().defaultValue(""));
            if (StringUtils.isNotBlank(stateCheckDir)) {
                // 默认Checkpoint,每隔 5 秒钟做一次 CK
                long interval = 5000L;
                if (flinkConfiguration.containsKey(ConfigConstant.EXECUTION_CHECKPOINT_INTERVAL)) {
                    // 设置用户配置
                    interval = flinkConfiguration.getLong(ConfigOptions.key(ConfigConstant.EXECUTION_CHECKPOINT_INTERVAL).longType().defaultValue(5000L));
                }
                env.enableCheckpointing(interval);
                // 指定 CK 的一致性语义
                env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
                // 设置任务关闭的时候保留最后一次 CK 数据
                env.getCheckpointConfig()
                        .enableExternalizedCheckpoints(
                                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
                // 指定从 CK 自动重启策略(尝试重启3次,每次间隔3秒)
                env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 3000L));
                // 设置状态保存
                // 保存方式('jobmanager', 'filesystem', 'rocksdb'),默认实现HDFS
                if (flinkConfiguration.containsKey(ConfigConstant.STATE_BACKEND_KEY)) {
                    // 设置用户配置
                    String backType = flinkConfiguration.getString(ConfigOptions.key(ConfigConstant.STATE_BACKEND_KEY).stringType().defaultValue("filesystem"));
                    if (!"filesystem".equals(backType)) {
                        // 等待填充(目前只有HDFS可选) fixme to jinmu
                    }
                }
                env.setStateBackend(new FsStateBackend(stateCheckDir));
            }
        }
        return env;
    }

    /**
     * 是否重启策略
     *
     * @param properties
     * @return java.util.Optional<java.lang.Boolean>
     * @author jinmu
     * @created 2022/4/26
     */
    public static Optional<Boolean> isRestore(Configuration properties) {
        String restoreEnable = properties.getString(ConfigConstant.RESTOREENABLE, "true");
        return Optional.of(Boolean.valueOf(restoreEnable));
    }

    /**
     * 重试间隔
     *
     * @param properties
     * @return
     */
    public static Optional<Integer> getDelayInterval(Configuration properties) {
        String delayInterval = properties.getString(ConfigConstant.DELAYINTERVAL, "10");
        return Optional.of(Integer.valueOf(delayInterval));
    }

    /**
     * 失败间隔
     *
     * @param properties
     * @return
     */
    public static Optional<Integer> getFailureInterval(Configuration properties) {
        String failureInterval = properties.getString(ConfigConstant.FAILUREINTERVAL, "6");
        return Optional.of(Integer.valueOf(failureInterval));
    }

    /**
     * getLocalStreamEnvironment
     *
     * @param flinkConfiguration
     * @return org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
     * @author jinmu
     * @created 2022/4/24
     */
    public static StreamExecutionEnvironment getLocalStreamEnvironment(
            Configuration flinkConfiguration) {
        FlinkDoLocalStreamEnvironment flinkDoLocalStreamEnvironment = new FlinkDoLocalStreamEnvironment(flinkConfiguration);
        // checkpoint setting(存在目录配置)
        if (flinkConfiguration.containsKey(ConfigConstant.CHECKPOINTS_DIRECTORY_KEY)) {
            LOG.debug("getLocalStreamEnvironment set checkpoint configs.");
            String stateCheckDir = flinkConfiguration.getString(ConfigOptions.key(ConfigConstant.CHECKPOINTS_DIRECTORY_KEY).stringType().defaultValue(""));
            if (StringUtils.isNotBlank(stateCheckDir)) {
                // 默认Checkpoint,每隔 5 秒钟做一次 CK
                long interval = 5000L;
                if (flinkConfiguration.containsKey(ConfigConstant.EXECUTION_CHECKPOINT_INTERVAL)) {
                    // 设置用户配置
                    interval = flinkConfiguration.getLong(ConfigOptions.key(ConfigConstant.EXECUTION_CHECKPOINT_INTERVAL).longType().defaultValue(5000L));
                }
                flinkDoLocalStreamEnvironment.enableCheckpointing(interval);
                // 指定 CK 的一致性语义
                flinkDoLocalStreamEnvironment.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
                // 设置任务关闭的时候保留最后一次 CK 数据
                flinkDoLocalStreamEnvironment.getCheckpointConfig()
                        .enableExternalizedCheckpoints(
                                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
                // 指定从 CK 自动重启策略(尝试重启3次,每次间隔3秒)
                flinkDoLocalStreamEnvironment.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 3000L));
                // 设置状态保存
                // 保存方式('jobmanager', 'filesystem', 'rocksdb'),默认实现HDFS
                if (flinkConfiguration.containsKey(ConfigConstant.STATE_BACKEND_KEY)) {
                    // 设置用户配置
                    String backType = flinkConfiguration.getString(ConfigOptions.key(ConfigConstant.STATE_BACKEND_KEY).stringType().defaultValue("filesystem"));
                    if (!"filesystem".equals(backType)) {
                        // 等待填充(目前只有HDFS可选) fixme to jinmu
                    }
                }
                flinkDoLocalStreamEnvironment.setStateBackend(new FsStateBackend(stateCheckDir));
            }
        }
        return flinkDoLocalStreamEnvironment;
    }

    /**
     * 构建StreamTableEnvironment
     *
     * @param jobOptions
     * @return org.apache.flink.table.api.bridge.java.StreamTableEnvironment
     * @author jinmu
     * @param: env
     * @created 2022/4/24
     */
    public static StreamTableEnvironment createStreamTableEnvironment(
            StreamExecutionEnvironment env, JobOptions jobOptions) throws Exception {
        if (null == jobOptions) {
            throw new FlinkDoRuntimeException("un setting jobOptions!");
        }
        EnvParams envParams = FlinkEnvUtils.loadEnvParams(jobOptions);
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);
        Configuration configuration = tEnv.getConfig().getConfiguration();

        Properties flinkRunProperties = PropertiesUtil.parseConf(envParams.getFlinkDynamicConf());
        // Iceberg need this config setting up true.
        configuration.setBoolean(
                TableConfigOptions.TABLE_DYNAMIC_TABLE_OPTIONS_ENABLED.key(), true);
        if (null != flinkRunProperties) {
            flinkRunProperties.entrySet().stream()
                    .filter(e -> e.getKey().toString().toLowerCase().startsWith("table."))
                    .forEach(
                            e ->
                                    configuration.setString(
                                            e.getKey().toString().toLowerCase(),
                                            e.getValue().toString().toLowerCase()));
        }
        // 动态配置属性 - 按照任务运行动态配置参数来设置
        streamTableEnvironmentStateTTLConfig(tEnv, flinkRunProperties);
        streamTableEnvironmentEarlyTriggerConfig(tEnv, flinkRunProperties);
        configuration.setString(PipelineOptions.NAME, jobOptions.getJobName());
        return tEnv;
    }

    /**
     * 设置TableEnvironment window提前触发
     *
     * @param tableEnv
     * @param confProperties
     */
    public static void streamTableEnvironmentEarlyTriggerConfig(
            TableEnvironment tableEnv, Properties confProperties) {
        String triggerTime = confProperties.getProperty(ConfigConstant.EARLY_TRIGGER);
        if (StringUtils.isNumeric(triggerTime)) {
            TableConfig qConfig = tableEnv.getConfig();
            qConfig.getConfiguration().setString("table.exec.emit.early-fire.enabled", "true");
            qConfig.getConfiguration()
                    .setString("table.exec.emit.early-fire.delay", triggerTime + "s");
        }
    }

    /**
     * 设置TableEnvironment状态超时时间
     *
     * @param tableEnv
     * @param confProperties
     */
    private static void streamTableEnvironmentStateTTLConfig(
            TableEnvironment tableEnv, Properties confProperties) {
        Optional<Tuple2<Time, Time>> tableEnvTTL = getTableEnvTTL(confProperties);
        if (tableEnvTTL.isPresent()) {
            Tuple2<Time, Time> timeRange = tableEnvTTL.get();
            TableConfig qConfig = tableEnv.getConfig();
            qConfig.setIdleStateRetentionTime(timeRange.f0, timeRange.f1);
        }
    }

    public static Optional<Tuple2<Time, Time>> getTableEnvTTL(Properties properties) {
        String ttlMintimeStr = properties.getProperty(ConfigConstant.SQL_TTL_MINTIME);
        String ttlMaxtimeStr = properties.getProperty(ConfigConstant.SQL_TTL_MAXTIME);
        if (StringUtils.isNotEmpty(ttlMintimeStr) || StringUtils.isNotEmpty(ttlMaxtimeStr)) {
            verityTtl(ttlMintimeStr, ttlMaxtimeStr);
            Matcher ttlMintimeStrMatcher = TTL_PATTERN.matcher(ttlMintimeStr);
            Matcher ttlMaxtimeStrMatcher = TTL_PATTERN.matcher(ttlMaxtimeStr);

            Long ttlMintime = 0L;
            Long ttlMaxtime = 0L;
            if (ttlMintimeStrMatcher.find()) {
                ttlMintime =
                        getTtlTime(
                                Integer.parseInt(ttlMintimeStrMatcher.group(1)),
                                ttlMintimeStrMatcher.group(2));
            }
            if (ttlMaxtimeStrMatcher.find()) {
                ttlMaxtime =
                        getTtlTime(
                                Integer.parseInt(ttlMaxtimeStrMatcher.group(1)),
                                ttlMaxtimeStrMatcher.group(2));
            }
            if (0L != ttlMintime && 0L != ttlMaxtime) {
                return Optional.of(
                        new Tuple2<>(Time.milliseconds(ttlMintime), Time.milliseconds(ttlMaxtime)));
            }
        }
        return Optional.empty();
    }

    /**
     * ttl 校验
     *
     * @param ttlMintimeStr 最小时间
     * @param ttlMaxtimeStr 最大时间
     */
    private static void verityTtl(String ttlMintimeStr, String ttlMaxtimeStr) {
        if (null == ttlMintimeStr
                || null == ttlMaxtimeStr
                || !TTL_PATTERN.matcher(ttlMintimeStr).find()
                || !TTL_PATTERN.matcher(ttlMaxtimeStr).find()) {
            throw new RuntimeException(
                    "sql.ttl.min 、sql.ttl.max must be set at the same time . example sql.ttl.min=1h,sql.ttl.max=2h");
        }
    }

    /**
     * 不同单位时间到毫秒的转换
     *
     * @param timeNumber 时间值，如：30
     * @param timeUnit   单位，d:天，h:小时，m:分，s:秒
     * @return
     */
    private static Long getTtlTime(Integer timeNumber, String timeUnit) {
        if ("d".equalsIgnoreCase(timeUnit)) {
            return timeNumber * 1000L * 60 * 60 * 24;
        } else if ("h".equalsIgnoreCase(timeUnit)) {
            return timeNumber * 1000L * 60 * 60;
        } else if ("m".equalsIgnoreCase(timeUnit)) {
            return timeNumber * 1000L * 60;
        } else if ("s".equalsIgnoreCase(timeUnit)) {
            return timeNumber * 1000L;
        } else {
            throw new RuntimeException("not support " + timeNumber + timeUnit);
        }
    }
}
