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
package com.jm.flink.conf;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Date: 2021/04/06 Company:
 *
 * @author
 */
public class DirtySettingConf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 脏数据文件的绝对路径，支持本地和hdfs
     */
    private String path;
    /**
     * hdfs时，Hadoop的配置
     */
    private Map<String, Object> hadoopConfig;
    /**
     * 脏数据对应的source端字段名称列表
     */
    private List<String> readerColumnNameList = Collections.emptyList();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getHadoopConfig() {
        return hadoopConfig;
    }

    public void setHadoopConfig(Map<String, Object> hadoopConfig) {
        this.hadoopConfig = hadoopConfig;
    }

    public List<String> getReaderColumnNameList() {
        return readerColumnNameList;
    }

    public void setReaderColumnNameList(List<String> readerColumnNameList) {
        this.readerColumnNameList = readerColumnNameList;
    }

    @Override
    public String toString() {
        return "DirtyConf{"
                + "path='"
                + path
                + '\''
                + ", hadoopConfig="
                + hadoopConfig
                + ", readerColumnNameList="
                + readerColumnNameList
                + '}';
    }
}