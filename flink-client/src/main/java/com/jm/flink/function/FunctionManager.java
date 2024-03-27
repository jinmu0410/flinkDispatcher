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

package com.jm.flink.function;

import com.jm.flink.base.exception.FlinkDoRuntimeException;
import com.jm.flink.base.exception.FlinkDoSqlParseException;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.table.functions.TableFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义函数管理类
 *
 * @author tasher
 * @created 2022/4/11
 * @return
 */
public class FunctionManager {

    private static final Logger logger = LoggerFactory.getLogger(FunctionManager.class);

    private static final String SCALAR = "SCALAR";

    private static final String TABLE = "TABLE";

    private static final String AGGREGATE = "AGGREGATE";

    private static void checkStreamTableEnv(TableEnvironment tableEnv) {
        if (!(tableEnv instanceof StreamTableEnvironment)) {
            throw new FlinkDoRuntimeException(
                    "no support tableEnvironment class for " + tableEnv.getClass().getName());
        }
    }

    /**
     * TABLE|SCALAR|AGGREGATE 注册UDF到table env
     *
     * @param classLoader
     * @return void
     * @author tasher
     * @param: type
     * @param: classPath
     * @param: funcName
     * @param: tableEnv
     * @created 2022/4/11
     */
    public static void registerUDF(
            String type,
            String classPath,
            String funcName,
            TableEnvironment tableEnv,
            ClassLoader classLoader) {
        if (SCALAR.equalsIgnoreCase(type)) {
            registerScalarUDF(classPath, funcName, tableEnv, classLoader);
        } else if (TABLE.equalsIgnoreCase(type)) {
            registerTableUDF(classPath, funcName, tableEnv, classLoader);
        } else if (AGGREGATE.equalsIgnoreCase(type)) {
            registerAggregateUDF(classPath, funcName, tableEnv, classLoader);
        } else {
            throw new FlinkDoSqlParseException(
                    "not support of UDF which is not in (TABLE, SCALAR, AGGREGATE)");
        }
    }

    /**
     * 注册自定义方法到env上
     *
     * @param classLoader
     * @return void
     * @author tasher
     * @param: classPath
     * @param: funcName
     * @param: tableEnv
     * @created 2022/4/11
     */
    public static void registerScalarUDF(
            String classPath, String funcName, TableEnvironment tableEnv, ClassLoader classLoader) {
        try {
            ScalarFunction scalarFunction =
                    Class.forName(classPath, false, classLoader)
                            .asSubclass(ScalarFunction.class)
                            .newInstance();
            tableEnv.registerFunction(funcName, scalarFunction);
            logger.info("register scalar function:{} success.", funcName);
        } catch (Exception e) {
            logger.error("", e);
            throw new FlinkDoRuntimeException("register UDF exception:", e);
        }
    }

    /**
     * 注册自定义TABLEFFUNC方法到env上
     *
     * @param classLoader
     * @return void
     * @author tasher
     * @param: classPath
     * @param: funcName
     * @param: tableEnv
     * @created 2022/4/11
     */
    @SuppressWarnings("rawtypes")
    public static void registerTableUDF(
            String classPath, String funcName, TableEnvironment tableEnv, ClassLoader classLoader) {
        try {
            checkStreamTableEnv(tableEnv);
            TableFunction tableFunction =
                    Class.forName(classPath, false, classLoader)
                            .asSubclass(TableFunction.class)
                            .newInstance();

            ((StreamTableEnvironment) tableEnv).registerFunction(funcName, tableFunction);
            logger.info("register table function:{} success.", funcName);
        } catch (Exception e) {
            logger.error("", e);
            throw new FlinkDoRuntimeException("register Table UDF exception:", e);
        }
    }

    /**
     * 注册自定义Aggregate FUNC方法到env上
     *
     * @param classLoader
     * @return void
     * @author tasher
     * @param: classPath
     * @param: funcName
     * @param: tableEnv
     * @created 2022/4/11
     */
    @SuppressWarnings("rawtypes")
    public static void registerAggregateUDF(
            String classPath, String funcName, TableEnvironment tableEnv, ClassLoader classLoader) {
        try {
            checkStreamTableEnv(tableEnv);

            AggregateFunction aggregateFunction =
                    Class.forName(classPath, false, classLoader)
                            .asSubclass(AggregateFunction.class)
                            .newInstance();
            ((StreamTableEnvironment) tableEnv).registerFunction(funcName, aggregateFunction);

            logger.info("register Aggregate function:{} success.", funcName);
        } catch (Exception e) {
            logger.error("", e);
            throw new FlinkDoRuntimeException("register Aggregate UDF exception:", e);
        }
    }
}
