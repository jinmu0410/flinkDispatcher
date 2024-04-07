package com.jm.flink.req;

import java.io.Serializable;

public class QueryJobStatusReq implements Serializable {

    private static final long serialVersionUID = -3098739564424675082L;
    /**
     * flink 任务ID
     */
    private String flinkJobId;

    /**
     * ON YARN集群ID
     */
    private String yarnApplicationId;

    private String webInterFaceUrl;

    private String yarnConfDir;

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

    public String getYarnConfDir() {
        return yarnConfDir;
    }

    public void setYarnConfDir(String yarnConfDir) {
        this.yarnConfDir = yarnConfDir;
    }
}
