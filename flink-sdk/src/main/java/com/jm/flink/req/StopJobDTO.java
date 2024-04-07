package com.jm.flink.req;

import java.io.Serializable;

/**
 * @author jinmu @ClassName CloseJobDTO.java @Description 停止已运行任务
 * @createTime 2022/05/02
 */
public class StopJobDTO implements Serializable {


    private static final long serialVersionUID = 6381602387137671435L;
    /**
     * on yarn applicationId
     */
    private String yarnApplicationId;

    /**
     * 关闭已提交的任务
     */
    private String flinkJobId;

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
}
