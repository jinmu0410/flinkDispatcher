package com.jm.flink.base.enums;

import com.jm.flink.base.CResultCode;

/**
 * 业务接口返回
 *
 * @author jinmu
 * @created 2022/5/2
 */
public enum CResultCodeEnum implements CResultCode {

    SUCCESS("200", "成功"),
    ERROR("500", "服务器错误"),
    BAD_REQUEST("400", "请求失败,%s"),
    NOT_LOGIN("401", "未登录"),
    BAD_PARAMS("402", "非法参数(%s)"),
    TOKEN_EXPIRATION("403", "token过期"),
    API_SIGNATURE_ERROR("441", "签名错误"),
    ILLEGAL_REQUEST("442", "非法请求"),
    ROLE_FORBIEEN("443", "角色权限不足"),
    PERMISSION_FORBIEEN("444", "操作权限不足"),
    DATA_FORBIEEN("445", "数据权限不足"),
    ;

    private String code;

    private String msg;

    CResultCodeEnum(String code, String msg) {

        this.code = code;
        this.msg = msg;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
