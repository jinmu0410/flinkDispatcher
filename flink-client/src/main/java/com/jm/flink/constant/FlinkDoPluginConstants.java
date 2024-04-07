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
package com.jm.flink.constant;


import com.jm.flink.base.enums.PluginMode;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

import static org.apache.flink.configuration.ConfigOptions.key;

/**
 * 配置常量
 *
 * @author jinmu
 * @created 2022/4/8
 */
public class FlinkDoPluginConstants extends FlinkDoConstants {

    // FlinkX log pattern
    public static final String DEFAULT_LOG4J_PATTERN =
            "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %-60c %x - %m%n";

    /**
     * 多少条打印一次日志
     */
    public static final String SAMPLE_INTERVAL_COUNT = "sample.interval.count";

    /**
     *
     */
    public static final ConfigOption<String> FLINK_PLUGIN_LOAD_MODE_KEY =
            ConfigOptions.key(FlinkDoConstants.KEY_PLUGIN_MODE)
                    .stringType()
                    .defaultValue(PluginMode.CLASSPATH.getName())
                    .withDescription(
                            "The config parameter defining YarnPer mode plugin loading method."
                                    + "classpath: The plugin package is not uploaded when the task is submitted. "
                                    + "The plugin package needs to be deployed in the pluginRoot directory of the yarn-node node, but the task starts faster"
                                    + "shipfile: When submitting a task, upload the plugin package under the pluginRoot directory to deploy the plug-in package. "
                                    + "The yarn-node node does not need to deploy the plugin package. "
                                    + "The task startup speed depends on the size of the plugin package and the network environment.");

    public static final String YARN_RESOURCE_MANAGER_WEBAPP_ADDRESS_KEY =
            "yarn.resourcemanager.webapp.address";

    // JobManager 进程总内存
    public static final ConfigOption<String> JM_MEM_PROCESS_MEMORY =
            key("jobmanager.memory.process.size")
                    .defaultValue("1024");


    // TaskManager 进程总内存
    public static final ConfigOption<String> TM_MEM_PROCESS_MEMORY =
            key("taskmanager.memory.process.size")
                    .defaultValue("1024");

    //任务管理器卡槽
    public static final ConfigOption<Integer> TM_NUM_TASK_SLOTS =
            key("taskmanager.numberOfTaskSlots")
                    .defaultValue(1);

    // 并行度
    public static final ConfigOption<Integer> PARALLELISM_DEFAULT =
            key("parallelism.default")
                    .defaultValue(1);
}
