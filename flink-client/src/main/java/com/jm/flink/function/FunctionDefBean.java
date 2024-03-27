package com.jm.flink.function;

import java.io.Serializable;

/**
 * @author tasher
 * @ClassName FunctionDefBean.java
 * @Description TODO
 * @createTime 2022/07/01
 */
public class FunctionDefBean implements Serializable {

    private static final long serialVersionUID = 1223558071069236278L;

    private String functionName;
    private String functionClass;
    private String functionLocation;
    private String functionFileMd5;
    private String localJarUrl;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionClass() {
        return functionClass;
    }

    public void setFunctionClass(String functionClass) {
        this.functionClass = functionClass;
    }

    public String getFunctionLocation() {
        return functionLocation;
    }

    public void setFunctionLocation(String functionLocation) {
        this.functionLocation = functionLocation;
    }

    public String getFunctionFileMd5() {
        return functionFileMd5;
    }

    public void setFunctionFileMd5(String functionFileMd5) {
        this.functionFileMd5 = functionFileMd5;
    }

    public String getLocalJarUrl() {
        return localJarUrl;
    }

    public void setLocalJarUrl(String localJarUrl) {
        this.localJarUrl = localJarUrl;
    }
}
