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


import com.jm.flink.base.exception.FlinkDoSqlParseException;
import com.jm.flink.function.FunctionDefBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.UserDefinedFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * SqlParserHelper.java
 *
 * @author tasher
 * @created 2022/5/5
 * @return
 */
public class SqlParserHelper {

    private static final Logger logger = LoggerFactory.getLogger(SqlParserHelper.class);

    private static final char SQL_DELIMITER = ';';

    /**
     * 解析器链路
     *
     * @return com.hk.szyc.flinkdo.main.core.sql.parse.AbstractStmtParser
     * @author tasher
     * @created 2022/4/11
     */
    private static AbstractStmtParser createParserChain() {

        AbstractStmtParser uploadFileStmtParser = new UploadFileStmtParser();
        AbstractStmtParser createFunctionStmtParser = new CreateFunctionStmtParser();
        AbstractStmtParser insertStmtParser = new InsertStmtParser();

        uploadFileStmtParser.setNextStmtParser(createFunctionStmtParser);
        createFunctionStmtParser.setNextStmtParser(insertStmtParser);

        return uploadFileStmtParser;
    }

    /**
     * getUseFulUDF
     *
     * @param functionDefBeans
     * @return java.util.List<com.hk.szyc.flinkdo.core.function.FunctionDefBean>
     * @author tasher
     * @param: sql
     * @created 2022/7/4
     */
    public static List<FunctionDefBean> getUseFulUDF(
            String sql, List<FunctionDefBean> functionDefBeans) {
        if (StringUtils.isBlank(sql)) {
            return Lists.newArrayList();
        }
        sql = SQLStringHelper.dealSqlComment(sql);
        String peekFunctionSql = sql.toLowerCase();
        List<FunctionDefBean> functionDefBeanPeek = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(functionDefBeans)) {
            for (FunctionDefBean functionDefBean : functionDefBeans) {
                if (peekFunctionSql.contains(functionDefBean.getFunctionName().toLowerCase())) {
                    functionDefBeanPeek.add(functionDefBean);
                }
            }
        }
        return functionDefBeanPeek;
    }

    /**
     * parseSql
     *
     * <p>flink support sql syntax CREATE TABLE sls_stream() with (); CREATE (TABLE|SCALA) FUNCTION
     * fcnName WITH customer; insert into tb1 select * from tb2;
     *
     * @param tableEnvironment
     * @return org.apache.flink.table.api.StatementSet
     * @author tasher
     * @param: sql
     * @param: urlList 应对的是自定义创建函数/添加JAR包
     * @created 2022/4/11
     */
    public static StatementSet parseSql(
            String sql, List<URL> urlList, List<FunctionDefBean> functionDefBeans, StreamTableEnvironment tableEnvironment) {

        if (StringUtils.isBlank(sql)) {
            throw new IllegalArgumentException("SQL must be not empty!");
        }

        sql = SQLStringHelper.dealSqlComment(sql);
        StatementSet statement = tableEnvironment.createStatementSet();
        Splitter splitter = new Splitter(SQL_DELIMITER);
        List<String> stmts = splitter.splitEscaped(sql);
        AbstractStmtParser stmtParser = createParserChain();

        if (CollectionUtils.isNotEmpty(functionDefBeans)) {
            // 注册函数
            URLClassLoader urlClassLoader = (URLClassLoader) SqlParserHelper.class.getClassLoader();
            functionDefBeans.forEach(functionDefBean -> {
                try {
                    UserDefinedFunction userDefinedFunction = Class.forName(functionDefBean.getFunctionClass(), false, urlClassLoader).asSubclass(UserDefinedFunction.class).newInstance();
                    tableEnvironment.createTemporarySystemFunction(functionDefBean.getFunctionName(), userDefinedFunction);
                    logger.debug("函数: {},类名: {},注册成功", functionDefBean.getFunctionName(), functionDefBean.getFunctionClass());
                } catch (Exception exception) {
                    logger.error("peek FunctionSql register udf function error:{}", exception.getMessage(), exception);
                }
            });
        }
        //  解析statement
        stmts.stream()
                .filter(stmt -> StringUtils.isNotBlank(stmt) && StringUtils.isNotBlank(stmt.trim()))
                .forEach(
                        stmt -> {
                            try {
                                stmtParser.handleStmt(stmt, tableEnvironment, statement, urlList);
                            } catch (Exception e) {
                                throw new FlinkDoSqlParseException(stmt, e);
                            }
                        });

        return statement;
    }

    public static void main(String[] args) {

        String allSql = "CREATE TABLE COVID19_data_source( \n" +
                "    Id  STRING, \n" +
                "    Province_State  STRING, \n" +
                "    Country_Region  STRING, \n" +
                "    Date  STRING, \n" +
                "    ConfirmedCases  STRING, \n" +
                "    Fatalities  STRING\n" +
                " )WITH(\n" +
                "    'scan.incremental.snapshot.enabled'='false',\n" +
                "    'server-time-zone'='Asia/Shanghai',\n" +
                "    'scan.startup.mode'='latest-offset',\n" +
                "    'scan.snapshot.fetch.size'='1024',\n" +
                "    'hostname'='192.168.100.250',\n" +
                "    'password'='Hk!1117#',\n" +
                "    'connect.timeout'='30',\n" +
                "    'connector'='mysql-cdc',\n" +
                "    'port'='3306',\n" +
                "    'database-name'='test_app',\n" +
                "    'table-name'='COVID19_data',\n" +
                "    'scan.incremental.snapshot.chunk.size'='8096',\n" +
                "    'username'='root'); \n" +
                "CREATE TABLE Topic_PaasJobSvc_N_LogTopic_RD_sink( \n" +
                "    Id  STRING, \n" +
                "    Province_State  STRING, \n" +
                "    Country_Region  STRING, \n" +
                "    Date  STRING, \n" +
                "    ConfirmedCases  STRING, \n" +
                "    Fatalities  STRING\n" +
                " )WITH(\n" +
                "    'properties.bootstrap.servers'='192.168.100.240:9092,192.168.100.241:9092',\n" +
                "    'debezium_json.timestamp-format.standard'='SQL',\n" +
                "    'connector'='kafka',\n" +
                "    'format'='debezium_json',\n" +
                "    'topic'='Topic-PaasJobSvc-N-LogTopic-RD'); \n" +
                "INSERT INTO Topic_PaasJobSvc_N_LogTopic_RD_sink SELECT \n" +
                " Id, \n" +
                "Province_State, \n" +
                "Country_Region, \n" +
                "Date, \n" +
                "ConfirmedCases, \n" +
                "Fatalities  FROM COVID19_data_source ;";
        Splitter splitter = new Splitter(SQL_DELIMITER);
        List<String> stmts = splitter.splitEscaped(allSql);
        if (null == stmts) {
            return;
        }
        for (String splitSql : stmts) {
            System.out.println("----------");
            System.out.println(splitSql);
        }
    }
}
