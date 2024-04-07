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
package com.jm.flink.constants;

/**
 * @author
 */
public class ConfigConstant {

    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CONFUSED_PASSWORD = "******";
    public static final String KEY_COLUMN = "column";

    public static final String SQL_TTL_MINTIME = "sql.ttl.min";
    public static final String SQL_TTL_MAXTIME = "sql.ttl.max";
    public static final String STRATEGY_NO_RESTART = "NoRestart";
    public static final String SQL_CHECKPOINT_INTERVAL_KEY = "sql.checkpoint.interval";
    public static final String SAVE_POINT_PATH_KEY = "savePointPath";
    public static final String ALLOW_NON_RESTORED_STATE_KEY = "allowNonRestoredState";

    // 执行恢复savepoint配置
    public static final String EXCUTION_SAVEPOINT_PATH = "execution.savepoint.path";

    // 执行恢复checkpoint配置


    // 保存savepoint dir配置
    /**
     * savepoint key
     */
    public static final String STATE_SAVE_POINT_DIR_KEY = "state.savepoints.dir";


    /**
     * checkpoint配置 key
     */
    // 保存方式
    public static final String STATE_BACKEND_KEY = "state.backend";
    // 保存方式
    public static final String STATE_CHECKPOINT_STORAGE = "state.checkpoint-storage";
    // 保存目录
    public static final String CHECKPOINTS_DIRECTORY_KEY = "state.checkpoints.dir";
    // 是否累加
    public static final String STATE_BACKEND_INCREMENTAL_KEY = "state.backend.incremental";
    // 执行check间隔
    public static final String EXECUTION_CHECKPOINT_INTERVAL = "execution.checkpointing.interval";
    // 执行check超时
    public static final String EXECUTION_CHECKPOINT_TIMEOUT = "execution.checkpointing.timeout";

    // 兼容上层
    public static final String FLINK_CHECKPOINT_INTERVAL_KEY = "flink.checkpoint.interval";


    public static final String FLINK_CHECKPOINT_MODE_KEY = "sql.checkpoint.mode";

    public static final String EXECUTION_CHECKPOINT_MODE = "execution.checkpointing.mode";

    public static final String FLINK_CHECKPOINT_TIMEOUT_KEY = "sql.checkpoint.timeout";


    public static final String FLINK_MAXCONCURRENTCHECKPOINTS_KEY =
            "execution.checkpointing.externalized-checkpoint-retention";

    public static final String SQL_CHECKPOINT_CLEANUPMODE_KEY = "sql.checkpoint.cleanup.mode";

    public static final String FLINK_CHECKPOINT_CLEANUPMODE_KEY = "flink.checkpoint.cleanup.mode";

    // checkpoint清理模式
    public static final String EXTERNALIZED_CHECKPOINT =
            "execution.checkpointing.externalized-checkpoint-retention";
    // FlinkX log pattern
    public static final String DEFAULT_LOG4J_PATTERN =
            "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %-60c %x - %m%n";

    // restart plocy
    public static final int FAILUEE_RATE = 3;

    public static final int FAILUEE_INTERVAL = 6; // min

    public static final int DELAY_INTERVAL = 10; // sec

    public static final String FAILUREINTERVAL = "failure.interval"; // min

    public static final String DELAYINTERVAL = "delay.interval"; // sec

    public static final String SQL_ENV_PARALLELISM = "sql.env.parallelism";

    public static final String SQL_MAX_ENV_PARALLELISM = "sql.max.env.parallelism";

    public static final String SQL_BUFFER_TIMEOUT_MILLIS = "sql.buffer.timeout.millis";

    public static final String EARLY_TRIGGER = "early.trigger";

    public static final String FLINK_TIME_CHARACTERISTIC_KEY = "time.characteristic";

    public static final String RESTOREENABLE = "restore.enable";
    public static final String AUTO_WATERMARK_INTERVAL_KEY = "autoWatermarkInterval";
    /**
     * 多少条打印一次日志
     */
    public static final String SAMPLE_INTERVAL_COUNT = "sample.interval.count";
}
