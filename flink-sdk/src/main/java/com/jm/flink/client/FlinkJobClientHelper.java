package com.jm.flink.client;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jm.flink.base.BizCodeEnum;
import com.jm.flink.base.JobConstants;
import com.jm.flink.base.Result;
import com.jm.flink.req.CancelJobReq;
import com.jm.flink.req.ExecProcessJobReq;
import com.jm.flink.req.QueryJobLogReq;
import com.jm.flink.req.QueryJobStatusReq;
import com.jm.flink.res.*;
import com.jm.flink.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.JobStatus;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.execution.SavepointFormatType;
import org.apache.flink.runtime.messages.Acknowledge;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.jm.flink.base.JobConstants.REST_ADDRESS;
import static com.jm.flink.base.JobConstants.REST_PORT;

public class FlinkJobClientHelper {

    private static final Logger LOG = LoggerFactory.getLogger(FlinkJobClientHelper.class);

    private static final Integer LOG_LINE_LIMIT = 500;

    /**
     * shell 脚本提交任务
     *
     * @param execProcessJobReq
     * @return
     */
    public static Result<ExecProcessJobRes> execJobInProcess(ExecProcessJobReq execProcessJobReq) {
        if (null == execProcessJobReq) {
            LOG.error("execJobInProcess, execProcessJobReq can't be empty");
            return Result.buildErrorResult();
        }
        if (execProcessJobReq.getJobOptions() == null) {
            LOG.error("execJobInProcess, req = {}", JSONUtils.toJSONString(execProcessJobReq));
            return Result.buildErrorResult();
        }
        try {
            String[] args = JobOptionParserUtil.transformArgsFromSortFieldArray(execProcessJobReq.getJobOptions());
            String taskId = UUID.randomUUID().toString().replace("-", "");

            // 运行参数
            String[] runArgs = {"java", JobConstants.JAR_JVM_ARGS, JobConstants.JAR_TIMEZONE_P, execProcessJobReq.getLogFile(), "-cp", execProcessJobReq.getJarClass(), JobConstants.MAIN_CLASS};

            Process process = Runtime.getRuntime().exec((String[]) ArrayUtils.addAll(runArgs, args));
            LOG.info("process id =({})", ProcessUtils.getProcessId(process));
            (new Thread(() -> {
                ProcessUtils.printStdLog(taskId, process);
            })).start();
            return Result.buildSuccessResult(new ExecProcessJobRes(ProcessUtils.getProcessId(process), process));
        } catch (IOException e) {
            LOG.error("execJobInProcess error: {}", e.getMessage(), e);
            return Result.buildErrorResult();
        }
    }

    /**
     * 取消任务
     *
     * @param cancelJobReq
     * @return
     */
    public static Result<CancelJobRes> cancelJob(CancelJobReq cancelJobReq) {
        if (null == cancelJobReq) {
            LOG.error("cancelJob, cancelJobReq can't be empty");
            Result.buildErrorResult();
        }
        if (StringUtils.isBlank(cancelJobReq.getFlinkJobId())
                || StringUtils.isBlank(cancelJobReq.getYarnApplicationId())
                || StringUtils.isBlank(cancelJobReq.getWebInterFaceUrl())) {
            LOG.error("cancelJob, req = {} ", JSONUtils.toJSONString(cancelJobReq));
            return Result.buildResult(BizCodeEnum.PARAM_ERROR);
        }
        Configuration defaultConfig = new Configuration();
        RestClusterClient restClusterClient = null;
        try {
            JobID jobId = JobID.fromHexString(cancelJobReq.getFlinkJobId());
            WebInterFaceRes webInterFaceRes = YarnRestUtil.buildWebInterFaceRes(cancelJobReq.getWebInterFaceUrl());
            if (null == webInterFaceRes) {
                return Result.buildResult(BizCodeEnum.JOB_CANCEL_ERROR);
            }
            defaultConfig.setString(REST_ADDRESS, webInterFaceRes.getIp());
            defaultConfig.setString(REST_PORT, webInterFaceRes.getPort());

            restClusterClient = new RestClusterClient(defaultConfig, cancelJobReq.getYarnApplicationId());

            boolean cancelResult;
            String savePointPath = "";
            if (StringUtils.isBlank(cancelJobReq.getSavePointDir())) {
                LOG.warn("not found the savepointDir setting,is skip!");
                Acknowledge acknowledge = (Acknowledge) restClusterClient.cancel(jobId).get();
                cancelResult = null != acknowledge;
            } else {
                if (cancelJobReq.getSavePointType() != null) {
                    switch (cancelJobReq.getSavePointType()) {
                        case TRIGGER:
                            savePointPath = (String) restClusterClient.triggerSavepoint(jobId, cancelJobReq.getSavePointDir(), SavepointFormatType.DEFAULT).get();
                            break;
                        case STOP:
                            savePointPath = (String) restClusterClient.stopWithSavepoint(jobId, false, cancelJobReq.getSavePointDir(), SavepointFormatType.DEFAULT).get();
                            break;
                        case CANCEL:
                            savePointPath = (String) restClusterClient.cancelWithSavepoint(jobId, cancelJobReq.getSavePointDir(), SavepointFormatType.DEFAULT).get();
                            break;
                        default:
                    }
                }
                cancelResult = StringUtils.isNotBlank(savePointPath);
            }
            if (!cancelResult) {
                return Result.buildResult(BizCodeEnum.JOB_CANCEL_ERROR);
            }
            return Result.buildSuccessResult(new CancelJobRes(savePointPath, jobId.toHexString()));
        } catch (Exception exception) {
            LOG.error("cancel job error:{}", exception.getMessage(), exception);
            return Result.buildErrorResult();
        } finally {
            if (null != restClusterClient) {
                restClusterClient.close();
            }
        }
    }

    /**
     * 查询任务状态 (10s超时)
     *
     * @param queryJobStatusReq
     * @return
     */
    public static Result<JobStatus> queryJobStatus(QueryJobStatusReq queryJobStatusReq) {
        if (null == queryJobStatusReq) {
            LOG.error("queryJobStatus, queryJobStatusReq can't be empty");
            return Result.buildResult(BizCodeEnum.PARAM_ERROR);
        }
        if (StringUtils.isBlank(queryJobStatusReq.getFlinkJobId())
                || StringUtils.isBlank(queryJobStatusReq.getYarnApplicationId())
                || StringUtils.isBlank(queryJobStatusReq.getWebInterFaceUrl())) {
            LOG.error("queryJobStatus , req = {}", JSONUtils.toJSONString(queryJobStatusReq));
            return Result.buildResult(BizCodeEnum.PARAM_ERROR);
        }
        Configuration defaultConfig = new Configuration();
        RestClusterClient restClusterClient = null;
        try {
            JobID jobId = JobID.fromHexString(queryJobStatusReq.getFlinkJobId());
            WebInterFaceRes webInterFaceBO = YarnRestUtil.buildWebInterFaceRes(queryJobStatusReq.getWebInterFaceUrl());
            if (null == webInterFaceBO) {
                return Result.buildResult(BizCodeEnum.JOB_QUERY_ERROR);
            }
            defaultConfig.setString(REST_ADDRESS, webInterFaceBO.getIp());
            defaultConfig.setString(REST_PORT, webInterFaceBO.getPort());
            restClusterClient = new RestClusterClient(defaultConfig, queryJobStatusReq.getYarnApplicationId());
            JobStatus jobStatus = (JobStatus) restClusterClient.getJobStatus(jobId).get(10, TimeUnit.SECONDS);
            if (null == jobStatus) {
                return Result.buildResult(BizCodeEnum.JOB_QUERY_ERROR);
            }
            return Result.buildSuccessResult(jobStatus);
        } catch (TimeoutException timeoutException) {
            LOG.error("query job status timeout error:{}", timeoutException.getMessage());
            //todo 查询flink任务状态超时，则再去yarn上查看任务状态
            if (org.apache.commons.lang3.StringUtils.isNotBlank(queryJobStatusReq.getYarnConfDir())) {
                YarnConfiguration yarnConfiguration = YarnConfLoaderUtil.loadYarnConfInDir(queryJobStatusReq.getYarnConfDir());
                yarnConfiguration.set("HADOOP.USER.NAME", "root");
                JobStatus jobStatus = YarnRestUtil.checkClusterInfo(queryJobStatusReq.getYarnApplicationId(), yarnConfiguration);
                return Result.buildSuccessResult(jobStatus);
            }
            return Result.buildErrorResult();
        } catch (Exception exception) {
            LOG.error("query job status error:{}", exception.getMessage(), exception);
            return Result.buildErrorResult();
        } finally {
            if (null != restClusterClient) {
                restClusterClient.close();
            }
        }
    }

    /**
     * 查询任务日志
     *
     * @param queryJobLogReq
     * @return
     */
    public static Result<JobLogRes> queryJobLog(QueryJobLogReq queryJobLogReq) {
        if (null == queryJobLogReq) {
            LOG.error("queryJobDTO can't be empty");
            return Result.buildResult(BizCodeEnum.PARAM_ERROR);
        }
        if (StringUtils.isBlank(queryJobLogReq.getFlinkJobId())) {
            LOG.error("flinkJobId can't be empty");
            return Result.buildResult(BizCodeEnum.JOB_LOG_QUERY_ERROR);
        }
        if (StringUtils.isBlank(queryJobLogReq.getYarnApplicationId())) {
            LOG.error("yarnApplicationId can't be empty");
            return Result.buildResult(BizCodeEnum.JOB_LOG_QUERY_ERROR);
        }
        if (StringUtils.isBlank(queryJobLogReq.getWebInterFaceUrl())) {
            LOG.error("webInterFaceUrl can't be empty");
            return Result.buildResult(BizCodeEnum.JOB_LOG_QUERY_ERROR);
        }
        if (StringUtils.isBlank(queryJobLogReq.getYarnConfDir())) {
            LOG.error("yarn config dir can't be empty");
            return Result.buildResult(BizCodeEnum.JOB_LOG_QUERY_ERROR);
        }
        if (null == queryJobLogReq.getLevel()) {
            queryJobLogReq.setLevel(LogLevel.INFO);
        }
        if (null == queryJobLogReq.getLogLimit() || queryJobLogReq.getLogLimit() <= 0) {
            queryJobLogReq.setLogLimit(LOG_LINE_LIMIT);
        }
        if (null == queryJobLogReq.getLogLimit() || queryJobLogReq.getLogLimit() == 0) {
            queryJobLogReq.setLogLimit(500);
        }

        // 先查Flink Web日志，逻辑上最新
        List<String> flinkLogs = queryJobInFlinkWeb(queryJobLogReq.getWebInterFaceUrl(), queryJobLogReq.getLogLimit(), queryJobLogReq.getLevel());
        if (CollectionUtils.isEmpty(flinkLogs)) {
            LOG.info("FlinkWeb查询失败,尝试查询集群日志");
            // 先检查集群任务状态
            YarnConfiguration yarnConfiguration = YarnConfLoaderUtil.loadYarnConfInDir(queryJobLogReq.getYarnConfDir());
            if (null == yarnConfiguration) {
                return Result.buildResult(BizCodeEnum.YARN_CONF_SETTING_ERROR);
            }
            boolean enableHa = yarnConfiguration.getBoolean(YarnConfiguration.RM_HA_ENABLED, false);
            String resourceUrl = "";
            if (!enableHa) {
                resourceUrl = yarnConfiguration.get(YarnConfiguration.RM_WEBAPP_ADDRESS, "");
            } else {
                resourceUrl = getActiveRMRestUrl(yarnConfiguration);
            }
            if (StringUtils.isBlank(resourceUrl)) {
                return Result.buildResult(BizCodeEnum.YARN_CONF_SETTING_ERROR);
            }
            Map<String, String> hostMapping = null;
            if (StringUtils.isNotBlank(queryJobLogReq.getYarnHostMapping())) {
                JSONUtils.parseObject(queryJobLogReq.getYarnHostMapping(), Map.class);
            }
            // 查询集群上的任务
            List<String> logList = queryJobManagerYarnLogs(resourceUrl, hostMapping, queryJobLogReq.getYarnApplicationId(), queryJobLogReq.getLogLimit());
            if (CollectionUtils.isNotEmpty(logList)) {
                return Result.buildSuccessResult(new JobLogRes(logList, queryJobLogReq.getLevel().getName()));
            }
            // 空日志
            return Result.buildSuccessResult(new JobLogRes(logList, queryJobLogReq.getLevel().getName()));
        }
        // 返回日志
        return Result.buildSuccessResult(new JobLogRes(flinkLogs, queryJobLogReq.getLevel().getName()));
    }

    public static List<String> queryJobManagerYarnLogs(String yarnResourceUrl, Map<String, String> hostMapping, String applicationId, Integer logLimit) {
        LOG.info("queryJobManagerYarnLogs yarnResourceUrl:url{}", yarnResourceUrl);
        YarnRestUtil yarnRestUtil = new YarnRestUtil(yarnResourceUrl, hostMapping);
        try {
            return yarnRestUtil.getClusterApplicationJobManagerLog(YarnRestUtil.LOG_TYPE_FLINK_JOBMANAGER, applicationId, logLimit);
        } catch (Exception exception) {
            LOG.error("getJobManagerYarnLogs error: {}", exception.getMessage(), exception);
        }
        return Lists.newArrayList();
    }

    public static List<String> queryJobInFlinkWeb(String webInterFaceUrl, Integer logLimit, LogLevel logLevel) {
        if (null == logLimit || logLimit == 0) {
            logLimit = 500;
        }
        String logFileUrl = webInterFaceUrl + "/jobmanager/log";
        if (LogLevel.INFO.equals(logLevel)) {
            logFileUrl = webInterFaceUrl + "/jobmanager/logs/jobmanager.log";
        } else if (LogLevel.ERROR.equals(logLevel)) {
            logFileUrl = webInterFaceUrl + "/jobmanager/logs/jobmanager.err";
        }
        LineNumberReader lineNumberReader = null;
        BufferedReader br = null;
        List<String> logLines = Lists.newArrayList();
        File tempFile = null;
        try {
            String tempFileName = "FlinkDo_temp_" + UUID.randomUUID().toString().replace("-", "");
            tempFile = File.createTempFile(tempFileName, ".log");
            org.apache.commons.io.FileUtils.copyURLToFile(new URL(logFileUrl), tempFile);
            if (tempFile.length() > 1024 * 1024 * 10) {
                LOG.error("任务日志查询失败, error: {}", BizCodeEnum.JOB_LOG_QUERY_LIMIT_ERROR.msg());
                return Lists.newArrayList();
            }
            logLines = FileUtils.readLines(tempFile, StandardCharsets.UTF_8);
            if (logLines.size() > logLimit) {
                logLines = logLines.subList(logLines.size() - logLimit, logLines.size());
            }
            return logLines;
        } catch (IOException e) {
            LOG.error("任务日志查询,FlinkWeb接口调用失败,error:{}", e.getMessage());
            return Lists.newArrayList();
        } finally {
            if (null != tempFile && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public static String getActiveRMRestUrl(YarnConfiguration yarnConfiguration) {
        if (null == yarnConfiguration) {
            return StringUtils.EMPTY;
        }
        // 获取所有HA 节点列表
        String[] rmIds = yarnConfiguration.getStrings(YarnConfiguration.RM_HA_IDS);
        for (String rmId : rmIds) {
            String apiUrl = yarnConfiguration.get(YarnConfiguration.RM_WEBAPP_ADDRESS.concat("." + rmId), StringUtils.EMPTY);
            if (StringUtils.isBlank(apiUrl)) {
                continue;
            }
            try {
                JSONObject nodeInfo = YarnRestUtil.getClusterInfoByNode(apiUrl);
                if (null == nodeInfo) {
                    continue;
                }
                String haState = nodeInfo.getString("haState");
                if ("ACTIVE".equals(haState)) {
                    return apiUrl;
                }
            } catch (Exception exception) {
                LOG.error("getActiveRMRestUrl node:{} error", rmId);
            }
        }
        return StringUtils.EMPTY;
    }
}
