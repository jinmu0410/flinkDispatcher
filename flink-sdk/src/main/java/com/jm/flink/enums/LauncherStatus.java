package com.jm.flink.enums;


import java.io.Serializable;

/**
 * @author jinmu @ClassName LauncherStatus.java
 * @Description program run status
 * @createTime 2022/02/18
 */
public enum LauncherStatus implements Serializable {

    /**
     * SUCCESS
     */
    SUCCESS(0, "运行成功"),

    /**
     * ERROR
     */
    ERROR(1, "失败退出"),

    /**
     * EXCEPTION
     */
    EXCEPTION(2, "异常退出"),

    /**
     * TIME_OUT
     */
    TIME_OUT(3, "超时退出"),

    /**
     * PARAM_ERROR
     */
    PARAM_ERROR(4, "参数错误");

    private int code;

    private String name;

    LauncherStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
