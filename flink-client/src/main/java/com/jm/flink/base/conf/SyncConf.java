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

import com.google.common.base.Preconditions;
import com.jm.flink.base.bean.JobParams;
import com.jm.flink.base.utils.GsonUtil;
import com.jm.flink.base.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Date: 2021/01/18 Company:
 *
 * @author
 */
public class SyncConf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Flink job
     */
    private SyncJobConf job;

    /**
     * 配置SpeedConf/Error/Restore等
     */
    private SettingConf settingConf = new SettingConf();

    /**
     * 具体任务插件路径
     */
    private String pluginRoot = "";

    private String restorePath;

    /**
     * 解析job字符串
     *
     * @param jobParamContent 围绕任务配置参数
     * @return com.hk.szyc.flinkdo.core.conf.SyncConf
     * @param: syncJobContent 同步任务配置内容
     * @author jinmu
     * @created 2022/4/25
     */
    public static SyncConf parseJob(String syncJobContent, String jobParamContent) {

        SyncConf syncConf = new SyncConf();

        // 同步任务配置
        SyncJobConf syncJobConf = JsonUtil.toObject(syncJobContent, SyncJobConf.class);
        syncConf.setJob(syncJobConf);

        // 任务配置
        SettingConf settingConf = new SettingConf();
        syncConf.settingConf = settingConf;
        JobParams jobParams = JsonUtil.toObject(jobParamContent, JobParams.class);
        if (null != jobParams) {
            // 脏数据
            settingConf.setDirty(jobParams.getDirtyConf());
            // 错误限制
            settingConf.setErrorLimit(jobParams.getErrorLimitConf());
            // 日志
            settingConf.setLog(jobParams.getLogConf());
            // 恢复任务配置
            settingConf.setRestart(jobParams.getRestartConf());
            // 监控插件
            settingConf.setMetricPluginConf(jobParams.getMetricPluginConf());
            // 续传
            settingConf.setRestore(jobParams.getRestoreConf());
            // 速率并行度
            settingConf.setSpeed(jobParams.getSpeedConf());
        }
        checkJob(syncConf);
        return syncConf;
    }

    /**
     * 校验Job配置
     *
     * @param config FlinkxJobConfig
     */
    private static void checkJob(SyncConf config) {

        // 检查reader配置
        OperatorConf reader = config.getReader();
        Preconditions.checkNotNull(
                reader,
                "[reader] in the task script is empty, please check the task script configuration.");
        String readerName = reader.getName();
        Preconditions.checkNotNull(
                readerName,
                "[name] under [reader] in task script is empty, please check task script configuration.");
        Map<String, Object> readerParameter = reader.getParameter();
        Preconditions.checkNotNull(
                readerParameter,
                "[parameter] under [reader] in the task script is empty, please check the configuration of the task script.");

        // 检查writer配置
        OperatorConf writer = config.getWriter();
        Preconditions.checkNotNull(
                writer,
                "[writer] in the task script is empty, please check the task script configuration.");
        String writerName = writer.getName();
        Preconditions.checkNotNull(
                writerName,
                "[name] under [writer] in the task script is empty, please check the configuration of the task script.");
        Map<String, Object> writerParameter = reader.getParameter();
        Preconditions.checkNotNull(
                writerParameter,
                "[parameter] under [writer] in the task script is empty, please check the configuration of the task script.");
        boolean transformer =
                config.getTransformer() != null
                        && StringUtils.isNotBlank(config.getTransformer().getTransformSql());
        if (transformer) {
            if (CollectionUtils.isEmpty(writer.getFieldList())) {
                throw new IllegalArgumentException(
                        "[column] under [writer] can not be empty when [transformSql] is not empty.");
            }
        }

        List<FieldConf> readerFieldList = config.getReader().getFieldList();
        // 检查并设置restore
        RestoreConf restore = config.getRestore();
        if (restore.isStream()) {
            // 实时任务restore必须设置为true，用于数据ck恢复
            restore.setRestore(true);
        } else if (restore.isRestore()) { // 离线任务开启断点续传
            FieldConf fieldColumnByName =
                    FieldConf.getSameNameMetaColumn(
                            readerFieldList, restore.getRestoreColumnName());
            FieldConf fieldColumnByIndex = null;
            if (restore.getRestoreColumnIndex() >= 0) {
                fieldColumnByIndex = readerFieldList.get(restore.getRestoreColumnIndex());
            }

            FieldConf fieldColumn;

            if (fieldColumnByName == null && fieldColumnByIndex == null) {
                throw new IllegalArgumentException(
                        "Can not find restore column from json with column name:"
                                + restore.getRestoreColumnName());
            } else if (fieldColumnByName != null
                    && fieldColumnByIndex != null
                    && fieldColumnByName != fieldColumnByIndex) {
                throw new IllegalArgumentException(
                        String.format(
                                "The column name and column index point to different columns, column name = [%s]，point to [%s]; column index = [%s], point to [%s].",
                                restore.getRestoreColumnName(),
                                fieldColumnByName,
                                restore.getRestoreColumnIndex(),
                                fieldColumnByIndex));
            } else {
                fieldColumn = fieldColumnByName != null ? fieldColumnByName : fieldColumnByIndex;
            }

            restore.setRestoreColumnIndex(fieldColumn.getIndex());
            restore.setRestoreColumnType(fieldColumn.getType());
        }
    }

    public OperatorConf getReader() {
        return job.getReader();
    }

    public OperatorConf getWriter() {
        return job.getWriter();
    }

    public TransformerConf getTransformer() {
        return job.getTransformer();
    }

    public SpeedConf getSpeed() {
        return settingConf.getSpeed();
    }

    public DirtySettingConf getDirty() {
        return settingConf.getDirty();
    }

    public ErrorLimitConf getErrorLimit() {
        return settingConf.getErrorLimit();
    }

    public LogConf getLog() {
        return settingConf.getLog();
    }

    public RestartConf getRestart() {
        return settingConf.getRestart();
    }

    public RestoreConf getRestore() {
        return settingConf.getRestore();
    }

    public SyncJobConf getJob() {
        return job;
    }

    public void setJob(SyncJobConf job) {
        this.job = job;
    }

    public String getRestorePath() {
        return restorePath;
    }

    public void setRestorePath(String restorePath) {
        this.restorePath = restorePath;
    }

    public MetricPluginConf getMetricPluginConf() {
        return settingConf.getMetricPluginConf();
    }

    public String getPluginRoot() {
        return pluginRoot;
    }

    public void setPluginRoot(String pluginRoot) {
        this.pluginRoot = pluginRoot;
    }

    @Override
    public String toString() {
        return "SysConf{" + "job=" + job + '\'' + ", restorePath='" + restorePath + '\'' + '}';
    }

    /**
     * 转换成字符串，不带job脚本内容
     *
     * @return
     */
    public String asString() {
        return "SysConf{" + "job=" + job + '\'' + ", restorePath='" + restorePath + '\'' + '}';
    }
}
