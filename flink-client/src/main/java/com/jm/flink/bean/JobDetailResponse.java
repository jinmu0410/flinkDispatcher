package com.jm.flink.bean;

import org.apache.flink.runtime.rest.messages.job.JobDetailsInfo;

import java.io.Serializable;

/**
 * @author tasher @ClassName RequestJobDetail.java @Description 任务详情
 * @createTime 2022/02/28
 */
@SuppressWarnings("rawtypes")
public class JobDetailResponse implements Serializable {

    private static final long serialVersionUID = 603216620778082252L;

    private JobDetailsInfo jobDetailsInfo;

    private Boolean result;

    public JobDetailResponse(boolean result, JobDetailsInfo jobDetailsInfo) {
        this.jobDetailsInfo = jobDetailsInfo;
        this.result = result;
    }

    public JobDetailsInfo getJobDetailsInfo() {
        return jobDetailsInfo;
    }

    public void setJobDetailsInfo(JobDetailsInfo jobDetailsInfo) {
        this.jobDetailsInfo = jobDetailsInfo;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
