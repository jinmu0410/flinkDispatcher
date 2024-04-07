package com.jm.flink.req;

import com.jm.flink.res.LogLevel;

import java.io.Serializable;

/**
 * @author jinmu @ClassName QueryJobLogDTO.java
 * @createTime 2022/05/02
 */
public class QueryJobLogReq implements Serializable {


    private static final long serialVersionUID = -1356045718989496568L;


    private String yarnConfDir;

    /**
     * json 对象
     */
    private String yarnHostMapping;

    /**
     * ON YARN集群ID
     */
    private String yarnApplicationId;

    /**
     * flink 任务ID
     */
    private String flinkJobId;

    /**
     * flink web url
     */
    private String webInterFaceUrl;

    /**
     * 返回行数限制，默认500
     */
    private Integer logLimit;

    /**
     * 日志查询级别(Error|Info)
     */
    private LogLevel level;

    public String getFlinkJobId() {
        return flinkJobId;
    }

    public void setFlinkJobId(String flinkJobId) {
        this.flinkJobId = flinkJobId;
    }

    public String getYarnApplicationId() {
        return yarnApplicationId;
    }

    public void setYarnApplicationId(String yarnApplicationId) {
        this.yarnApplicationId = yarnApplicationId;
    }

    public String getWebInterFaceUrl() {
        return webInterFaceUrl;
    }

    public void setWebInterFaceUrl(String webInterFaceUrl) {
        this.webInterFaceUrl = webInterFaceUrl;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public Integer getLogLimit() {
        return logLimit;
    }

    public void setLogLimit(Integer logLimit) {
        this.logLimit = logLimit;
    }

    public String getYarnConfDir() {
        return yarnConfDir;
    }

    public void setYarnConfDir(String yarnConfDir) {
        this.yarnConfDir = yarnConfDir;
    }

    public String getYarnHostMapping() {
        return yarnHostMapping;
    }

    public void setYarnHostMapping(String yarnHostMapping) {
        this.yarnHostMapping = yarnHostMapping;
    }
}
