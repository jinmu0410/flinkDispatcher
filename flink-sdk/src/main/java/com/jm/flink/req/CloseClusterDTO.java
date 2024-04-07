package com.jm.flink.req;

import java.io.Serializable;

/**
 * @author jinmu @ClassName CloseClusterDTO.java
 * @createTime 2022/05/02
 */
public class CloseClusterDTO implements Serializable {


    private static final long serialVersionUID = -5936716340074156993L;
    /**
     * ON YARN集群ID
     */
    private String yarnApplicationId;


    private String yarnConfDir;

    /**
     * json 对象
     */
    private String yarnHostMapping;

    public String getYarnApplicationId() {
        return yarnApplicationId;
    }

    public void setYarnApplicationId(String yarnApplicationId) {
        this.yarnApplicationId = yarnApplicationId;
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
