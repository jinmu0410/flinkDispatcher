package com.jm.flink.base;

/**
 * 返回结果接口定义
 *
 * @author tasher
 * @created 2022/5/2
 */
public interface CResultCode {

    /**
     * 业务code
     *
     * @return
     */
    String code();

    /**
     * 业务信息 支持String.format占位符
     *
     * @returna
     */
    String msg();
}