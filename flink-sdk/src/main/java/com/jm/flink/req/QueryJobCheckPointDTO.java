package com.jm.flink.req;

import java.io.Serializable;

/**
 * @author jinmu @ClassName QueryJobCheckPointDTO.java
 * @createTime 2022/05/02
 */
public class QueryJobCheckPointDTO implements Serializable {


    private static final long serialVersionUID = -423136815101057128L;
    /**
     * flink 任务ID
     */
    private String flinkJobId;

    private String webInterFaceUrl;

    public String getFlinkJobId() {
        return flinkJobId;
    }

    public void setFlinkJobId(String flinkJobId) {
        this.flinkJobId = flinkJobId;
    }

    public String getWebInterFaceUrl() {
        return webInterFaceUrl;
    }

    public void setWebInterFaceUrl(String webInterFaceUrl) {
        this.webInterFaceUrl = webInterFaceUrl;
    }
}
