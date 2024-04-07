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
import java.util.Map;

/**
 * 远程存储配置
 *
 * @author jinmu
 * @created 2022/6/30
 */
public class RemoteStorageConf implements Serializable {


    private static final long serialVersionUID = -5641701590841508288L;

    private String storageType = "hdfs";

    private String udfPath;

    /**
     * 备用: 其他插件使用
     */
    private String pluginPath;

    /**
     * 具体连接或关键配置信息，如hadoopConfig
     * <p>
     * "fs.defaultFS": "hdfs://0.0.0.0:9000",
     * "dfs.ha.namenodes.ns1" : "nn1,nn2",
     * "dfs.namenode.rpc-address.ns1.nn1" : "0.0.0.0:9000",
     * "dfs.namenode.rpc-address.ns1.nn2" : "0.0.0.1:9000",
     * "dfs.client.failover.proxy.provider.ns1" : "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider",
     * "dfs.nameservices" : "ns1",
     * "hadoop.user.name": "",
     * 授权相关配置
     * <p>
     * useLocalFile: true;
     * principalFile(keytab.keytab);
     * principal: 授权用户账号;
     * java.security.krb5.conf: krb5Conf;
     */
    private Map<String, Object> storagePros;


    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public Map<String, Object> getStoragePros() {
        return storagePros;
    }

    public void setStoragePros(Map<String, Object> storagePros) {
        this.storagePros = storagePros;
    }

    public String getUdfPath() {
        return udfPath;
    }

    public void setUdfPath(String udfPath) {
        this.udfPath = udfPath;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
