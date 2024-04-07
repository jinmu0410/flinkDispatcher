package com.jm.flink.res;

import java.io.Serializable;
import java.util.List;

public class JobLogRes implements Serializable {

    private static final long serialVersionUID = 4578149974351358708L;

    private List<String> logs;

    private String level;

    public JobLogRes(List<String> logs, String level) {
        this.logs = logs;
        this.level = level;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
