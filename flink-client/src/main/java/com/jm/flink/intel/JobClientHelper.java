package com.jm.flink.intel;


import com.jm.flink.bean.JobSubmitResponse;
import com.jm.flink.helper.JobDeployHelper;

/**
 * 客户端任务接口类
 *
 * @author jinmu
 * @created 2022/4/8
 */
public interface JobClientHelper {

    /**
     * 提交任务
     *
     * @return
     * @throws Exception
     */
    JobSubmitResponse submit(JobDeployHelper jobDeployHelper) throws Exception;
}
