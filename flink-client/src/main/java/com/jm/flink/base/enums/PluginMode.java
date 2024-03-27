package com.jm.flink.base.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author tasher @ClassName FlinkPluginMode.java @Description TODO
 * @createTime 2022/02/18
 */
public enum PluginMode {

    /**
     * FlinkPluginMode in the classpath
     */
    CLASSPATH(0, "classpath"),

    /**
     * FlinkPluginMode in the shipfile
     */
    SHIPFILE(1, "shipfile");

    private int type;

    private String name;

    PluginMode(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static PluginMode getByName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("FlinkPluginMode name cannot be null or empty");
        }
        switch (name) {
            case "classpath":
                return CLASSPATH;
            default:
                return SHIPFILE;
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
