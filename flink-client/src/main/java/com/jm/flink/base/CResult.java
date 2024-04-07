package com.jm.flink.base;

import com.jm.flink.base.enums.CResultCodeEnum;

import java.io.Serializable;

/**
 * 业务结果集
 *
 * @author jinmu
 * @created 2022/5/2
 */
@SuppressWarnings("unchecked")
public class CResult<T> implements Serializable {

    private String code;
    private String msg;
    private T data;

    public CResult() {
    }

    public CResult(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean isSuccess() {
        return CResultCodeEnum.SUCCESS.code().equals(this.code);
    }

    @SuppressWarnings("rawtypes")
    private static final class ResultBuilder<T> {

        private String code;
        private String msg;
        private T data;

        private ResultBuilder() {
        }

        private static ResultBuilder newResult() {
            return new ResultBuilder();
        }

        private ResultBuilder<T> buildCode(String code) {
            this.code = code;
            return this;
        }

        private ResultBuilder<T> buildMsg(String msg) {
            this.msg = msg;
            return this;
        }

        private ResultBuilder<T> buildData(T data) {
            this.data = data;
            return this;
        }

        private CResult<T> build() {
            CResult<T> CResult = new CResult();
            CResult.setCode(code);
            CResult.setMsg(msg);
            CResult.setData(data);
            return CResult;
        }
    }

    public static <T> CResult<T> buildSuccessResult() {
        return ResultBuilder.newResult()
                .buildCode(CResultCodeEnum.SUCCESS.code())
                .buildMsg(CResultCodeEnum.SUCCESS.msg())
                .build();
    }

    public static <T> CResult<T> buildErrorResult() {
        return ResultBuilder.newResult()
                .buildCode(CResultCodeEnum.ERROR.code())
                .buildMsg(CResultCodeEnum.ERROR.msg())
                .build();
    }

    public static <T> CResult<T> buildResult(CResultCode resultCode) {
        return ResultBuilder.newResult()
                .buildCode(resultCode.code())
                .buildMsg(resultCode.msg())
                .build();
    }

    public static <T> CResult<T> buildResult(CResultCode resultCode, T data) {
        return ResultBuilder.newResult()
                .buildCode(resultCode.code())
                .buildMsg(resultCode.msg())
                .buildData(data)
                .build();
    }

    public static <T> CResult<T> buildSuccessResult(T data) {
        return ResultBuilder.newResult()
                .buildCode(CResultCodeEnum.SUCCESS.code())
                .buildMsg(CResultCodeEnum.SUCCESS.msg())
                .buildData(data)
                .build();
    }

    public static <T> CResult<T> buildErrorResult(T data) {
        return ResultBuilder.newResult()
                .buildCode(CResultCodeEnum.ERROR.code())
                .buildMsg(CResultCodeEnum.ERROR.msg())
                .buildData(data)
                .build();
    }

    public static <T> CResult<T> buildErrorResult(String msg, T data) {
        return ResultBuilder.newResult()
                .buildCode(CResultCodeEnum.ERROR.code())
                .buildMsg(msg)
                .buildData(data)
                .build();
    }

    public static <T> CResult<T> buildErrorResult(String msg) {
        return ResultBuilder.newResult()
                .buildCode(CResultCodeEnum.ERROR.code())
                .buildMsg(msg)
                .build();
    }

    @SuppressWarnings("rawtypes")
    public static <T> CResult<T> copyErrorResult(CResult sourceCResult) {
        return ResultBuilder.newResult()
                .buildCode(sourceCResult.getCode())
                .buildMsg(sourceCResult.getMsg())
                .build();
    }
}
