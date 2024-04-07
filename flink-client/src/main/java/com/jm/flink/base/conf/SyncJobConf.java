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
package com.jm.flink.base.conf;


import com.jm.flink.base.utils.GsonUtil;
import com.jm.flink.base.utils.JsonUtil;

import java.io.Serializable;

/**
 * 插件同步任务使用
 *
 * @author jinmu
 */
public class SyncJobConf implements Serializable {

    private static final long serialVersionUID = -8499515460059751463L;

    /**
     * source reader配置
     */
    private OperatorConf reader;

    /**
     * sink writer配置
     */
    private OperatorConf writer;

    /**
     * sql transformer配置
     */
    private TransformerConf transformer;

    public OperatorConf getReader() {
        return reader;
    }

    public void setReader(OperatorConf reader) {
        this.reader = reader;
    }

    public OperatorConf getWriter() {
        return writer;
    }

    public void setWriter(OperatorConf writer) {
        this.writer = writer;
    }

    public TransformerConf getTransformer() {
        return transformer;
    }

    public void setTransformer(TransformerConf transformer) {
        this.transformer = transformer;
    }

    @Override
    public String toString() {
        // 直接json输出
        return JsonUtil.toJson(this);
    }
}
