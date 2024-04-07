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

package com.jm.flink.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jm.flink.res.WebInterFaceRes;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.JobStatus;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class YarnRestUtil {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(YarnRestUtil.class);

    // flink 任务日志
    public static final String LOG_TYPE_FLINK_JOBMANAGER = "jobmanager.log";

    private String resetApiUrl = "localhost:8088";

    private Map<String, String> hostMapping = Maps.newHashMap();

    private final static String CLUSTER_APPATTEMPTS_URL = "%s/ws/v1/cluster/apps/%s/appattempts";

    private final static String CLUSTER_INFO = "%s/ws/v1/cluster/info";


    public YarnRestUtil(String resetApiUrl, Map<String, String> hostMapping) {
        this.resetApiUrl = resetApiUrl;
        this.hostMapping = hostMapping;
        if (!this.resetApiUrl.startsWith("http")) {
            this.resetApiUrl = "http://" + this.resetApiUrl;
        }
    }

    /**
     * 查询集群Cluster Application AppattemptsInfo
     *
     * @param applicationId
     * @return com.alibaba.fastjson.JSONObject
     * @author jinmu
     * @created 2022/5/10
     */
    public JSONObject getClusterApplicationAppattemptsInfo(String applicationId) throws Exception {
        if (StringUtils.isBlank(applicationId)) {
            return null;
        }
        String getUrl = String.format(CLUSTER_APPATTEMPTS_URL, this.resetApiUrl, applicationId);
        getUrl = transUrlWithHostMapping(getUrl);
        HttpEntity result = HttpClientUtils.doGet(getUrl);
        if (null != result) {
            String responseResult = EntityUtils.toString(result);
            if (StringUtils.isBlank(responseResult)) {
                return null;
            }
            return JSONUtils.parseObject(responseResult, JSONObject.class);
        }
        return null;
    }

    /**
     * 检查检点活跃度
     *
     * @param rmHost
     * @return com.alibaba.fastjson.JSONObject
     * @author jinmu
     * @created 2022/5/21
     */
    public static JSONObject getClusterInfoByNode(String rmHost) throws Exception {

        if (StringUtils.isBlank(rmHost)) {
            return null;
        }
        String getUrl = String.format(CLUSTER_INFO, transIpPort(rmHost));
        log.info("getUrl url:{}",getUrl);
        HttpEntity result = HttpClientUtils.doGet(getUrl, RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(5000).build());
        if (null != result) {
            String responseResult = EntityUtils.toString(result);
            if (StringUtils.isBlank(responseResult)) {
                return null;
            }
            JSONObject clusterInfo = JSONUtils.parseObject(responseResult, JSONObject.class);
            if(null!= clusterInfo){
                return clusterInfo.getJSONObject("clusterInfo");
            }
        }
        return null;
    }

    /**
     * 查询指定LOG TYPE 日志
     *
     * @param logLimit
     * @return java.util.List<java.lang.String>
     * @author jinmu
     * @param: applicationId
     * @created 2022/5/10
     */
    public List<String> getClusterApplicationJobManagerLog(String logType, String applicationId, Integer logLimit) throws Exception {

        if (null == logLimit || logLimit == 0) {
            logLimit = 500;
        }
        JSONObject appattemptsInfo = this.getClusterApplicationAppattemptsInfo(applicationId);
        if (null == appattemptsInfo || !appattemptsInfo.containsKey("appAttempts")) {
            return Lists.newArrayList();
        }
        String buildLogUrl = null;
        JSONObject appAttempts = appattemptsInfo.getJSONObject("appAttempts");
        if (null != appAttempts && appAttempts.containsKey("appAttempt")) {
            JSONArray appAttempt = appAttempts.getJSONArray("appAttempt");
            if (null != appAttempt && appAttempt.size() > 0) {
                JSONObject currentFirstInfo = appAttempt.getJSONObject(0);
                String logUrl = currentFirstInfo.getString("logsLink");
                Long startTime = currentFirstInfo.getLong("startTime");
                Long endTime = currentFirstInfo.getLong("finishedTime");
                buildLogUrl = String.format("%s/%s?start=0&start.time=%s", logUrl, logType, startTime);
            }
        }
        buildLogUrl = transUrlWithHostMapping(buildLogUrl);
        log.info("getClusterApplicationJobManagerLog url: {}", buildLogUrl);
        String logHtml = EntityUtils.toString(HttpClientUtils.doGet(buildLogUrl));
        Document htmlDoc = Jsoup.parse(logHtml);
        String log = htmlDoc.select("table > tbody > tr > td.content >pre").get(0).text();
        if (StringUtils.isBlank(log)) {
            return Lists.newArrayList();
        }
        String[] logs = log.split("\n");
        if (logs.length > logLimit) {
            logs = (String[]) ArrayUtils.subarray(logs, logs.length - logLimit, logs.length);
        }
        return Lists.newArrayList(logs);
    }

    public List<String> getClusterApplications() throws Exception {
        return Lists.newArrayList();
    }

    /**
     * hostname IP映射转换
     *
     * @param url
     * @return java.lang.String
     * @author jinmu
     * @created 2022/5/10
     */
    private String transUrlWithHostMapping(String url) {
        if (this.hostMapping == null || this.hostMapping.keySet().size() == 0) {
            return url;
        }
        for (String key : hostMapping.keySet()) {
            String mappingIp = this.hostMapping.get(key);
            url = url.replaceAll(key, mappingIp);
        }
        return url;
    }

    private static String transIpPort(String ipPort) {
        if (!ipPort.startsWith("http")) {
            ipPort = "http://" + ipPort;
        }
        return ipPort;
    }

    public static WebInterFaceRes buildWebInterFaceRes(String webInterfaceUrl) {
        if (StringUtils.isBlank(webInterfaceUrl)) {
            return null;
        }
        webInterfaceUrl = webInterfaceUrl.replace("http://", "");
        webInterfaceUrl = webInterfaceUrl.replace("https://", "");
        int hasPort = webInterfaceUrl.lastIndexOf(":");
        String ip = "localhost";
        String port = "";
        if (hasPort != -1) {
            ip = webInterfaceUrl.substring(0, hasPort);
            port = webInterfaceUrl.substring(hasPort + 1, webInterfaceUrl.length());
        } else {
            ip = webInterfaceUrl;
        }
        return new WebInterFaceRes(ip, port);
    }

    /**
     * 检查集群信息
     *
     * @param yarnConfiguration
     * @return org.apache.flink.api.common.JobStatus
     * @author tasher
     * @param: applicationId
     * @created 2022/5/10
     */
    public static JobStatus checkClusterInfo(String applicationId, YarnConfiguration yarnConfiguration) {
        ApplicationId queryAppId = buildApplicationInstance(applicationId);
        YarnClient yarnClient = null;
        try {
            yarnClient = YarnClient.createYarnClient();
            yarnClient.init(yarnConfiguration);
            yarnClient.start();
            List<ApplicationReport> applicationReports = yarnClient.getApplications();
            if (CollectionUtils.isNotEmpty(applicationReports)) {
                for (ApplicationReport applicationReport : applicationReports) {
                    if (applicationReport.getApplicationId().equals(queryAppId)) {
                        return caseYarnStatus(applicationReport.getYarnApplicationState());
                    }
                }
            }
        } catch (YarnException | IOException e) {
            log.error("checkClusterInfo error: {}", e.getMessage());
        } finally {
            if (null != yarnClient) {
                yarnClient.stop();
            }
        }
        return JobStatus.FAILED;
    }
    public static ApplicationId buildApplicationInstance(String applicationIdStr) {
        if (StringUtils.isBlank(applicationIdStr)
                || !applicationIdStr.startsWith(ApplicationId.appIdStrPrefix)
                || !applicationIdStr.contains("_")) {
            return null;
        }
        return ConverterUtils.toApplicationId(applicationIdStr);
    }
    /**
     * 转换YARN STATUS => JOB STATUS
     *
     * @param yarnApplicationState
     * @return
     */
    public static JobStatus caseYarnStatus(YarnApplicationState yarnApplicationState) {
        switch (yarnApplicationState) {
            case FAILED:
                return JobStatus.FAILED;
            case FINISHED:
                return JobStatus.FINISHED;
            case RUNNING:
                return JobStatus.RUNNING;
            default:
                return JobStatus.FAILED;
        }
    }
}
