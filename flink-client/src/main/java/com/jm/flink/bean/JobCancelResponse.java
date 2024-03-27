package com.jm.flink.bean;

import java.io.Serializable;

/**
 * @author tasher @ClassName CallBackCancelJob.java @Description 任务提交结果提交结果
 * @createTime 2022/02/28
 */
@SuppressWarnings("rawtypes")
public class JobCancelResponse implements Serializable {

    private static final long serialVersionUID = 3413347405791087187L;

    private String savePointPath;

    private Boolean result;

    public JobCancelResponse(boolean result, String savePointPath) {
        this.savePointPath = savePointPath;
        this.result = result;
    }

    public String getSavePointPath() {
        return savePointPath;
    }

    public void setSavePointPath(String savePointPath) {
        this.savePointPath = savePointPath;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
