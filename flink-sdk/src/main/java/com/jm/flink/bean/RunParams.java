package com.jm.flink.bean;

import java.io.Serializable;

/**
 * @author jinmu @ClassName RunParams.java
 * @Description 程序运行参数
 */
public class RunParams implements Serializable {


    private static final long serialVersionUID = 4219428723808373110L;

    /**
     * 运行超时时间(单位秒)
     */
    private Long launchTimeOut;

    /**
     * 其他应用程序附加参数
     */
    private String programParams;

    public Long getLaunchTimeOut() {
        return launchTimeOut;
    }

    public void setLaunchTimeOut(Long launchTimeOut) {
        this.launchTimeOut = launchTimeOut;
    }

    public String getProgramParams() {
        return programParams;
    }

    public void setProgramParams(String programParams) {
        this.programParams = programParams;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
