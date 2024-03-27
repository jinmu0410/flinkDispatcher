package com.jm.flink.base.bean;


import com.jm.flink.base.conf.*;

import java.io.Serializable;

/**
 * @author tasher @ClassName JobParams.java @Description 任务运行相关参数
 * @createTime 2022/04/11
 */
public class JobParams implements Serializable {

    private static final long serialVersionUID = -5787452920691734476L;

    /**
     * 自定义yarnApplicationId
     */
    private String yarnApplicationId;

    /**
     * 自定义flinkJobId
     */
    private String flinkJobId;

    /**
     * 速率及通道配置
     */
    private SpeedConf speedConf = new SpeedConf();
    /**
     * 任务运行时数据读取写入的出错控制
     */
    private ErrorLimitConf errorLimitConf = new ErrorLimitConf();
    /**
     * 任务指标插件信息
     */
    private MetricPluginConf metricPluginConf = new MetricPluginConf();

    /**
     * 断点续传配置
     */
    private RestoreConf restoreConf = new RestoreConf();

    /**
     * 失败重试配置
     */
    private RestartConf restartConf = new RestartConf();

    /**
     * 日志记录配置
     */
    private LogConf logConf = new LogConf();

    private String formatType;

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getFormatType() {
        return formatType;
    }

    /**
     * 脏数据保存配置
     */
    private DirtySettingConf dirtySettingConf = new DirtySettingConf();

    public String getYarnApplicationId() {
        return yarnApplicationId;
    }

    public void setYarnApplicationId(String yarnApplicationId) {
        this.yarnApplicationId = yarnApplicationId;
    }

    public String getFlinkJobId() {
        return flinkJobId;
    }

    public void setFlinkJobId(String flinkJobId) {
        this.flinkJobId = flinkJobId;
    }

    public SpeedConf getSpeedConf() {
        return speedConf;
    }

    public void setSpeedConf(SpeedConf speedConf) {
        this.speedConf = speedConf;
    }

    public ErrorLimitConf getErrorLimitConf() {
        return errorLimitConf;
    }

    public void setErrorLimitConf(ErrorLimitConf errorLimitConf) {
        this.errorLimitConf = errorLimitConf;
    }

    public MetricPluginConf getMetricPluginConf() {
        return metricPluginConf;
    }

    public void setMetricPluginConf(MetricPluginConf metricPluginConf) {
        this.metricPluginConf = metricPluginConf;
    }

    public RestoreConf getRestoreConf() {
        return restoreConf;
    }

    public void setRestoreConf(RestoreConf restoreConf) {
        this.restoreConf = restoreConf;
    }

    public RestartConf getRestartConf() {
        return restartConf;
    }

    public void setRestartConf(RestartConf restartConf) {
        this.restartConf = restartConf;
    }

    public LogConf getLogConf() {
        return logConf;
    }

    public void setLogConf(LogConf logConf) {
        this.logConf = logConf;
    }

    public DirtySettingConf getDirtyConf() {
        return dirtySettingConf;
    }

    public void setDirtyConf(DirtySettingConf dirtySettingConf) {
        this.dirtySettingConf = dirtySettingConf;
    }
}
