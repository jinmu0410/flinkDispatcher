package com.jm.flink.res;

public enum LogLevel {
    /**
     * info level
     */
    INFO(0, "Info"),
    /**
     * Error level
     */
    ERROR(1, "Error");

    private int type;

    private String name;

    LogLevel(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
