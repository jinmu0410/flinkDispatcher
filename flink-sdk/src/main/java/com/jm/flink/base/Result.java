package com.jm.flink.base;

import java.io.Serializable;

/**
 * 业务结果集
 *
 * @author jinmu
 * @created 2022/5/2
 */
@SuppressWarnings("unchecked")
public class Result<T> implements Serializable {

    private String code;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(String code) {
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
        return ResultCodeEnum.SUCCESS.code().equals(this.code);
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

        private Result<T> build() {
            Result<T> Result = new Result();
            Result.setCode(code);
            Result.setMsg(msg);
            Result.setData(data);
            return Result;
        }
    }

    public static <T> Result<T> buildSuccessResult() {
        return ResultBuilder.newResult()
                .buildCode(ResultCodeEnum.SUCCESS.code())
                .buildMsg(ResultCodeEnum.SUCCESS.msg())
                .build();
    }

    public static <T> Result<T> buildErrorResult() {
        return ResultBuilder.newResult()
                .buildCode(ResultCodeEnum.ERROR.code())
                .buildMsg(ResultCodeEnum.ERROR.msg())
                .build();
    }

    public static <T> Result<T> buildResult(ResultCode resultCode) {
        return ResultBuilder.newResult()
                .buildCode(resultCode.code())
                .buildMsg(resultCode.msg())
                .build();
    }

    public static <T> Result<T> buildResult(ResultCode resultCode, T data) {
        return ResultBuilder.newResult()
                .buildCode(resultCode.code())
                .buildMsg(resultCode.msg())
                .buildData(data)
                .build();
    }

    public static <T> Result<T> buildSuccessResult(T data) {
        return ResultBuilder.newResult()
                .buildCode(ResultCodeEnum.SUCCESS.code())
                .buildMsg(ResultCodeEnum.SUCCESS.msg())
                .buildData(data)
                .build();
    }

    public static <T> Result<T> buildErrorResult(T data) {
        return ResultBuilder.newResult()
                .buildCode(ResultCodeEnum.ERROR.code())
                .buildMsg(ResultCodeEnum.ERROR.msg())
                .buildData(data)
                .build();
    }

    public static <T> Result<T> buildErrorResult(String msg, T data) {
        return ResultBuilder.newResult()
                .buildCode(ResultCodeEnum.ERROR.code())
                .buildMsg(msg)
                .buildData(data)
                .build();
    }

    public static <T> Result<T> buildErrorResult(String msg) {
        return ResultBuilder.newResult()
                .buildCode(ResultCodeEnum.ERROR.code())
                .buildMsg(msg)
                .build();
    }

    @SuppressWarnings("rawtypes")
    public static <T> Result<T> copyErrorResult(Result sourceResult) {
        return ResultBuilder.newResult()
                .buildCode(sourceResult.getCode())
                .buildMsg(sourceResult.getMsg())
                .build();
    }
}
