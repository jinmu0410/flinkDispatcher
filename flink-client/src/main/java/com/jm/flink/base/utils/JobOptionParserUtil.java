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

import com.google.common.collect.Lists;
import com.jm.flink.base.bean.JobOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.MapperFeature;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析任务参数
 *
 * @author jinmu
 * @created 2022/4/9
 */
public class JobOptionParserUtil {

    private static Logger logger = LoggerFactory.getLogger(JobOptionParserUtil.class);

    private static final String OPTION_JOB = "job";

    private final Options options = new Options();

    private final DefaultParser parser = new DefaultParser();

    private final JobOptions properties = new JobOptions();

    private static final ObjectMapper objectMapper =
            new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public JobOptionParserUtil(String[] args) throws Exception {
        Class cla = properties.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            OptionRequired optionRequired = field.getAnnotation(OptionRequired.class);
            if (optionRequired != null) {
                options.addOption(name, optionRequired.hasArg(), optionRequired.description());
            }
        }
        CommandLine cl = parser.parse(options, args);
        for (Field field : fields) {
            String name = field.getName();
            String value = cl.getOptionValue(name);
            OptionRequired optionRequired = field.getAnnotation(OptionRequired.class);
            if (optionRequired != null) {
                if (optionRequired.required() && StringUtils.isBlank(value)) {
                    throw new RuntimeException(String.format("parameters of %s is required", name));
                }
            }
            if (StringUtils.isNotBlank(value)) {
                field.setAccessible(true);
                field.set(properties, value);
            }
        }
    }

    public JobOptionParserUtil(String[] args, boolean isDecoder) throws Exception {
        Class cla = properties.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            OptionRequired optionRequired = field.getAnnotation(OptionRequired.class);
            if (optionRequired != null) {
                options.addOption(name, optionRequired.hasArg(), optionRequired.description());
            }
        }
        CommandLine cl = parser.parse(options, args);
        for (Field field : fields) {
            String name = field.getName();
            String value = cl.getOptionValue(name);
            if (isDecoder && null != value) {
                value = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
            }
            OptionRequired optionRequired = field.getAnnotation(OptionRequired.class);
            if (optionRequired != null) {
                if (optionRequired.required() && StringUtils.isBlank(value)) {
                    throw new RuntimeException(String.format("parameters of %s is required", name));
                }
            }
            if (StringUtils.isNotBlank(value)) {
                field.setAccessible(true);
                field.set(properties, value);
            }
        }
    }

    public static List<String> transformOptionsArgs(JobOptions parseOptions) throws IOException {
        if (null == parseOptions) {
            return Lists.newArrayList();
        }
        HashMap<String, Object> argsMap =
                objectMapper.readValue(
                        objectMapper.writeValueAsString(parseOptions), HashMap.class);
        if (null == argsMap) {
            return Lists.newArrayList();
        }
        List<String> args = new ArrayList<>();
        for (Map.Entry<String, Object> stringEntry : argsMap.entrySet()) {
            String key = stringEntry.getKey();
            Object value = stringEntry.getValue();
            if (value == null || StringUtils.isBlank(String.valueOf(value))) {
                continue;
            }
            args.add("-" + key);
            args.add(value.toString());
        }
        return args;
    }

    public static HashMap<String, Object> transformOptionsToMap(JobOptions parseOptions)
            throws IOException {
        if (null == parseOptions) {
            return null;
        }
        HashMap<String, Object> argsMap =
                objectMapper.readValue(
                        objectMapper.writeValueAsString(parseOptions), HashMap.class);
        return argsMap;
    }

    public static String[] transformArgsFromSortFieldArray(JobOptions parseOptions)
            throws IOException {
        if (null == parseOptions) {
            return null;
        }
        HashMap<String, Object> argsMap =
                objectMapper.readValue(
                        objectMapper.writeValueAsString(parseOptions), HashMap.class);
        if (null == argsMap) {
            return new String[0];
        }
        Class cla = JobOptions.class;
        Field[] fields = cla.getDeclaredFields();
        List<String> fieldValues = Lists.newArrayList();
        for (Field field : fields) {
            String name = field.getName();
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            Object value = argsMap.get(name);
            String valueStr = "";
            if (null != value) {
                valueStr = String.valueOf(value);
            }
            if (name.equals("jobContent")) {
                valueStr = "" + valueStr;
            }
            fieldValues.add("-" + name);
            fieldValues.add(URLEncoder.encode(valueStr, StandardCharsets.UTF_8.name()));
        }
        return fieldValues.toArray(new String[0]);
    }

    public JobOptions getJobOptions() {
        return properties;
    }
}
