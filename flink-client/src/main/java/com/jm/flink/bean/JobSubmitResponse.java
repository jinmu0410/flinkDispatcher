package com.jm.flink.bean;

import org.apache.flink.core.execution.JobClient;

import java.io.Serializable;

/**
 * @author jinmu @ClassName CallBackSubmit.java @Description 提交结果
 * @createTime 2022/02/28
 */
@SuppressWarnings("rawtypes")
public class JobSubmitResponse implements Serializable {

    private static final long serialVersionUID = -6117322038394227075L;

    private String jobId;

    private String yarnApplicationId;

    /**
     * 本地任务会需要回传用来，关闭任务
     */
    private JobClient jobClient;

    private String webInterfaceUrl;

    public JobSubmitResponse(String jobId) {
        this.jobId = jobId;
    }

    public JobSubmitResponse() {
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public JobClient getJobClient() {
        return jobClient;
    }

    public void setJobClient(JobClient jobClient) {
        this.jobClient = jobClient;
    }

    public String getWebInterfaceUrl() {
        return webInterfaceUrl;
    }

    public void setWebInterfaceUrl(String webInterfaceUrl) {
        this.webInterfaceUrl = webInterfaceUrl;
    }

    public String getYarnApplicationId() {
        return yarnApplicationId;
    }

    public void setYarnApplicationId(String yarnApplicationId) {
        this.yarnApplicationId = yarnApplicationId;
    }
}
