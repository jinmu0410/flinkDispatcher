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

import com.google.common.base.Charsets;

import com.jm.flink.base.exception.FlinkDoRuntimeException;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 属性配置解析
 *
 * @author jinmu
 * @created 2022/4/9
 */
public class PropertiesUtil {

    /**
     * 解析properties json字符串
     *
     * @param confStr
     * @return java.util.Properties
     * @author jinmu
     * @created 2022/4/9
     */
    public static Properties parseConf(String confStr) {
        if (StringUtils.isEmpty(confStr)) {
            return new Properties();
        }
        try {
            confStr = URLDecoder.decode(confStr, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return new Properties();
        }
        return JsonUtil.toObject(confStr, Properties.class);
    }

    public static Map<String, String> confToMap(String confStr) {
        if (StringUtils.isEmpty(confStr)) {
            return new HashMap();
        }

        try {
            confStr = URLDecoder.decode(confStr, Charsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new FlinkDoRuntimeException(e);
        }
        return JsonUtil.toObject(confStr, Map.class);
    }

    /**
     * Properties key value去空格
     *
     * @param confProperties
     * @return java.util.Properties
     * @author jinmu
     * @created 2022/4/9
     */
    public static Properties propertiesTrim(Properties confProperties) {
        Properties properties = new Properties();
        confProperties.forEach((k, v) -> properties.put(k.toString().trim(), v.toString().trim()));
        return properties;
    }
}
