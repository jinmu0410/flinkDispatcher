package com.jm.flink.res;

import java.io.Serializable;


public class CancelJobRes implements Serializable {


    private static final long serialVersionUID = -4409472983113277738L;

    private String savePointPath;

    private String jobId;

    public CancelJobRes(String savePointPath, String jobId) {
        this.savePointPath = savePointPath;
        this.jobId = jobId;
    }

    public String getSavePointPath() {
        return savePointPath;
    }

    public void setSavePointPath(String savePointPath) {
        this.savePointPath = savePointPath;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
