package com.jm.flink.helper;

import com.jm.flink.base.utils.JobOptionParserUtil;
import com.jm.flink.bean.JobSubmitResponse;
import com.jm.flink.intel.JobClientHelper;
import com.jm.flink.main.Main;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.core.execution.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * 本地任务提交
 *
 * @author tasher
 * @created 2022/4/8
 */
public class LocalJobClientHelper implements JobClientHelper {

    private static final Logger logger = LoggerFactory.getLogger(LocalJobClientHelper.class);

    @Override
    public JobSubmitResponse submit(JobDeployHelper jobDeployHelper) throws Exception {
        // option => String[] args
        List<String> execArgs =
                JobOptionParserUtil.transformOptionsArgs(jobDeployHelper.getJobOptions());
        if (CollectionUtils.isEmpty(execArgs)) {
            return new JobSubmitResponse(null);
        }
        Optional<JobClient> jobClient = Main.localNativeJob(jobDeployHelper.getJobOptions());
        if (null != jobClient && jobClient.isPresent()) {
            return new JobSubmitResponse(jobClient.get().getJobID().toHexString());
        }
        return new JobSubmitResponse(null);
    }
}
