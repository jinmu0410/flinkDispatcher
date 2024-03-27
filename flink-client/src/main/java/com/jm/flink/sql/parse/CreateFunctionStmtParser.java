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

package com.jm.flink.sql.parse;


import com.jm.flink.base.classloader.ClassLoaderManager;
import com.jm.flink.base.exception.FlinkDoSqlParseException;
import com.jm.flink.function.FunctionManager;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建函数解析器
 *
 * @author tasher
 * @created 2022/4/11
 */
public class CreateFunctionStmtParser extends AbstractStmtParser {

    private static final String FUNC_PATTERN_STR =
            "(?i)\\s*CREATE\\s+(scalar|table|aggregate)\\s+FUNCTION\\s+(\\S+)\\s+WITH\\s+(\\S+)";

    private static final Pattern FUNC_PATTERN = Pattern.compile(FUNC_PATTERN_STR);

    @Override
    public boolean canHandle(String stmt) {
        return FUNC_PATTERN.matcher(stmt).find();
    }

    @Override
    public void execStmt(
            String stmt,
            StreamTableEnvironment tEnv,
            StatementSet statementSet,
            List<URL> jarUrlList) {
        if (FUNC_PATTERN.matcher(stmt).find()) {
            Matcher matcher = FUNC_PATTERN.matcher(stmt);
            if (matcher.find()) {
                String type = matcher.group(1);
                String funcName = matcher.group(2);
                String className = matcher.group(3);
                // 请让前置ClassLoader存在
                ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    URLClassLoader classLoader =
                            ClassLoaderManager.loadExtraJar(
                                    jarUrlList, (URLClassLoader) currentClassLoader);
                    // 注册自定义函数
                    FunctionManager.registerUDF(type, className, funcName, tEnv, classLoader);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new FlinkDoSqlParseException(e);
                }
            }
        }
    }
}
