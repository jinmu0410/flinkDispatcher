package com.jm.flink.res;

import java.io.Serializable;

/**
 * @author tasher @ClassName WebInterFaceBO.java
 * @createTime 2022/02/28
 */
@SuppressWarnings("rawtypes")
public class WebInterFaceRes implements Serializable {


    private static final long serialVersionUID = -5977702475479626445L;

    private String ip;

    private String port;

    public WebInterFaceRes(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
