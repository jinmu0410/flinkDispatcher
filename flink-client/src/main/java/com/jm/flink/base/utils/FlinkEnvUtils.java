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
package com.jm.flink.base.utils;


import com.jm.flink.base.bean.EnvParams;
import com.jm.flink.base.bean.JobOptions;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * FlinkEnvUtils
 *
 * <p>=== 对于简单进行总内存管理，采用以下配置之一 指定TaskManager进程的总内存: taskmanager.memory.process.size 或 指定Flink总内存:
 * taskmanager.memory.flink.size
 *
 * <p>=== 对于生产环境服务环境明确采用这种方式 指定JVM堆内存大小: taskmanager.memory.task.heap.size: 2048m 指定JVM托管内存大小:
 * taskmanager.memory.managed.size: 512m
 *
 * @author jinmu
 * @created 2022/4/11
 * @return
 */
public class FlinkEnvUtils {

    private static final Logger logger = LoggerFactory.getLogger(FlinkEnvUtils.class);

    /**
     * 默认YARN 用户
     */
    private static final String DEFAULT_DEPLOY_USER = "root";

    /**
     * 加载环境参数
     *
     * @param jobOptions
     * @return com.hk.szyc.flinkdo.core.base.bean.EnvParams
     * @author jinmu
     * @created 2022/4/15
     */
    public static EnvParams loadEnvParams(JobOptions jobOptions) {
        if (null == jobOptions || StringUtils.isBlank(jobOptions.getEnvParams())) {
            return new EnvParams();
        }
        EnvParams envParams = null;
        try {
            envParams = JsonUtil.toObject(StringUtil.toURLDecode(jobOptions.getEnvParams()), EnvParams.class);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (null == envParams) {
            return new EnvParams();
        }
        return envParams;
    }
}
