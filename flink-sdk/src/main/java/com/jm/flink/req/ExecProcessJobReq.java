package com.jm.flink.req;



import com.jm.flink.bean.JobOptions;

import java.io.Serializable;

public class ExecProcessJobReq implements Serializable {


    private static final long serialVersionUID = -27059693419093302L;
    /**
     * 任务参数
     */
    private JobOptions jobOptions;

    /**
     * 执行目录
     */
    private String shell;

    /**
     * JAR包绝对路径
     */
    private String jarClass;

    private String logFile;

    public JobOptions getJobOptions() {
        return jobOptions;
    }

    public void setJobOptions(JobOptions jobOptions) {
        this.jobOptions = jobOptions;
    }

    public String getShell() {
        return shell;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public String getJarClass() {
        return jarClass;
    }

    public void setJarClass(String jarClass) {
        this.jarClass = jarClass;
    }
}
