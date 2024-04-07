package com.jm.flink.req;

import java.io.Serializable;

/**
 * @author jinmu @ClassName QueryJobDTO.java
 * @createTime 2022/05/02
 */
public class QueryJobDTO implements Serializable {


    private static final long serialVersionUID = -4541258107110433727L;
    /**
     * flink 任务ID
     */
    private String flinkJobId;

    /**
     * ON YARN集群ID
     */
    private String yarnApplicationId;

    private String webInterFaceUrl;

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
}
