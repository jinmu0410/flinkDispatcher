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

import java.io.Serializable;

/**
 * 配置常量
 *
 * @author jinmu
 * @created 2022/4/8
 */
public class FlinkDoConstants implements Serializable {

    private static final long serialVersionUID = 48396987803642219L;

    public static final String KEY_FLINK_HOME = "FLINK_HOME";
    public static final String KEY_HADOOP_HOME = "HADOOP_HOME";
    /**
     * 插件加载方式KEY,贯穿使用在整个作业配置中，不要随意改动
     */
    public static final String KEY_PLUGIN_MODE = "Plugin_Load_Mode";

    public static final String KEY_HOST_PREX = "Plugin_HOST_";

    public static final String FLINK_VERSION_DIST_JAR = "/flink-dist-1.17.1.jar";

    public static final int MIN_JM_MEMORY = 1024;
    public static final int MIN_TM_MEMORY = 1024;
    public static final String JOBMANAGER_MEMORY_MB = "jobmanager.memory.process.size";
    public static final String TASKMANAGER_MEMORY_MB = "taskmanager.memory.process.size";
    public static final String SLOTS_PER_TASKMANAGER = "taskmanager.slots";
    public static final String REGX_FLINK_DIST_NAME = "flink-dist";

    public static final String FORMATS_SUFFIX = "formats";
    public static final String READER_SUFFIX = "reader";
    private static final String JAR_SUFFIX = ".jar";
    public static final String SOURCE_SUFFIX = "source";
    public static final String WRITER_SUFFIX = "writer";
    public static final String SINK_SUFFIX = "sink";
    public static final String GENERIC_SUFFIX = "Factory";
    public static final String METRIC_SUFFIX = "metrics";
    public static final String DEFAULT_METRIC_PLUGIN = "prometheus";

    public static final String MAIN_CLASS = "com.hk.szyc.flinkdo.core.main.Main";

    /**
     * 插件class前缀格式化
     */
    public static final String CLASS_FILE_NAME_FMT = "class_path_%d";

    public static final String CLASS_FILE_NAME_FMT_STR = "class_path_%s";

    /**
     * 插件class匹配
     */
    public static final String CLASS_FILE_NAME_FMT_PREFIX = "class_path_";

    public static final String FILE_PATH_SPLIT = "/";

    public static final String FILE_PATH_HDFS = "hdfs://";

    /**
     * 命令行运行参数纷纷
     */
    public static final String RUN_COMMA_SYMBOL = ",";

    public static final String RUN_EQUAL_SYMBOL = ";";


}
