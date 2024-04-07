package com.jm.flink.base;

/**
 * 业务接口返回 10000
 *
 * @author jinmu
 * @created 2022/5/2
 */
public enum BizCodeEnum implements ResultCode {

    // 任务类
    JOB_NAME_ERROR("10001", "任务名称不能为空"),
    JOB_CONTENT_ERROR("10002", "执行内容不能为空"),
    JOB_TYPE_ERROR("10003", "任务类型不能为空"),
    JOB_TYPE_VALID("10004", "任务类型无效,可选[sql|sync]"),
    JOB_DEPLOY_ERROR("10005", "任务提交模式错误,可选[local|yarn-session|yarn-per-job]"),
    JOB_SUBMIT_LIMIT("10006", "任务提交上限，请稍后重试"),
    JOB_EXEC_ERROR("10007", "任务执行异常，请稍后重试"),
    JOB_CANCEL_ERROR("10008", "任务取消失败"),
    JOB_QUERY_ERROR("10009", "任务查询失败，缺少关键参数"),
    PARAM_ERROR("100010", "缺少关键参数"),
    JOB_LOG_QUERY_LIMIT_ERROR("100011", "文件超过限制，查询失败"),
    JOB_LOG_QUERY_ERROR("100012", "系统错误，查询日志失败"),
    JOB_QUERY_ACCU_ERROR("100013", "任务聚合信息查询失败"),
    JOB_QUERY_CHECKPOINT_ERROR("10014", "任务检查点查询失败，缺少关键参数"),
    YARN_CONF_SETTING_ERROR("10015", "设置的Hadoop配置文件错误"),
    YARN_APPLICATION_CLOSE_ERROR("10016", "Yarn Application 关闭错误");

    private final String code;

    private final String msg;

    BizCodeEnum(String code, String msg) {

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
