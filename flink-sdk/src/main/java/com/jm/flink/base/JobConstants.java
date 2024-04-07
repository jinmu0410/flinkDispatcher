package com.jm.flink.base;


/**
 * @author jinmu
 * @description 任务常量
 * @date 2022/5/11
 */
public class JobConstants {

    /**
     * 任务运行Client
     */
    public static final String MAIN_CLASS=  "com.jm.flink.main.Main";

    public static final String JAR_JVM_ARGS = "-Xmx768m";

    public static final String JAR_TIMEZONE_P= "-Duser.timezone=GMT+08";

    public static final String REST_ADDRESS = "rest.address";
    public static final String REST_PORT = "rest.port";

}
