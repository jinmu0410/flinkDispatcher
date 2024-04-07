package com.jm.flink.req;

import java.io.Serializable;

/**
 * @author jinmu @ClassName QueryClusterDTO.java
 * @createTime 2022/05/02
 */
public class QueryClusterDTO implements Serializable {


    private static final long serialVersionUID = -3799666986465825097L;

    private String yarnConfDir;

    /**
     * json 对象
     */
    private String yarnHostMapping;


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
