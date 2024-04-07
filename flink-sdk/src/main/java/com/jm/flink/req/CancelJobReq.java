package com.jm.flink.req;


import com.jm.flink.base.SavePointType;

import java.io.Serializable;

public class CancelJobReq implements Serializable {

    private static final long serialVersionUID = 8733451356206254887L;

    /**
     * on yarn applicationId
     */
    private String yarnApplicationId;

    /**
     * 接口地址
     */
    private String webInterFaceUrl;

    /**
     * 取消已提交的
     */
    private String flinkJobId;

    /**
     * 保存点的目录
     */
    private String savePointDir;

    /**
     * CANONICAL
     * NATIVE
     */
    private String savepointFormatStr;

    private SavePointType savePointType;


    public String getWebInterFaceUrl() {
        return webInterFaceUrl;
    }

    public void setWebInterFaceUrl(String webInterFaceUrl) {
        this.webInterFaceUrl = webInterFaceUrl;
    }

    public String getFlinkJobId() {
        return flinkJobId;
    }

    public void setFlinkJobId(String flinkJobId) {
        this.flinkJobId = flinkJobId;
    }

    public String getYarnApplicationId() {
        return yarnApplicationId;
    }

    public void setYarnApplicationId(String yarnApplicationId) {
        this.yarnApplicationId = yarnApplicationId;
    }

    public String getSavePointDir() {
        return savePointDir;
    }

    public void setSavePointDir(String savePointDir) {
        this.savePointDir = savePointDir;
    }

    public void setSavepointFormatStr(String savepointFormatStr) {
        this.savepointFormatStr = savepointFormatStr;
    }

    public String getSavepointFormatStr() {
        return savepointFormatStr;
    }

    public void setSavePointType(SavePointType savePointType) {
        this.savePointType = savePointType;
    }

    public SavePointType getSavePointType() {
        return savePointType;
    }
}
