package com.jm.flink.res;

import java.io.Serializable;

public class ExecProcessJobRes implements Serializable {


    private static final long serialVersionUID = -339063464574668006L;
    private Long processId;

    private Process process;

    public ExecProcessJobRes(Long processId, Process process) {
        this.processId = processId;
        this.process = process;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }
}
