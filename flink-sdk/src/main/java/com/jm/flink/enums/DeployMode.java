package com.jm.flink.enums;


import org.apache.commons.lang.StringUtils;

/**
 * @author jinmu @ClassName DeployMode.java @Description flink run env
 * @createTime 2022/02/18
 */
public enum DeployMode {

    /**
     * Applications executed in the local
     */
    local(0, "local"),

    /**
     * Applications executed in the standalone
     */
    standalone(1, "standalone"),

    /**
     * Applications executed in the yarn session
     */
    yarnSession(2, "yarn-session"),

    /**
     * Applications executed in the yarn perjob
     */
    yarnPerJob(3, "yarn-per-job"),

    /**
     * Applications executed in the yarn application
     */
    yarnApplication(4, "yarn-application"),

    /**
     * Applications executed in the kubernetes session
     */
    kubernetesSession(5, "kubernetes-session"),

    /**
     * Applications executed in the kubernetes perjob
     */
    kubernetesPerJob(6, "kubernetes-per-job"),

    /**
     * Applications executed in the kubernetes application
     */
    kubernetesApplication(7, "kubernetes-application");

    private int type;

    private String name;

    DeployMode(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static DeployMode getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return local;
        }
        switch (name) {
            case "standalone":
                return standalone;
            case "yarn":
            case "yarn-session":
                return yarnSession;
            case "yarnPer":
            case "yarn-per-job":
                return yarnPerJob;
            case "yarn-application":
                return yarnApplication;
            case "kubernetes-session":
                return kubernetesSession;
            case "kubernetes-per-job":
                return kubernetesPerJob;
            case "kubernetes-application":
                return kubernetesApplication;
            default:
                return local;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
