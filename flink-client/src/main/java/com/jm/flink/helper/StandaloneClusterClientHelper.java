package com.jm.flink.helper;


import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.bean.JobSubmitResponse;
import com.jm.flink.intel.JobClientHelper;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.StandaloneClusterDescriptor;
import org.apache.flink.client.deployment.StandaloneClusterId;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单机应用环境
 *
 * @author jinmu
 * @created 2022/4/8
 */
public class StandaloneClusterClientHelper implements JobClientHelper {

    private static final Logger LOG = LoggerFactory.getLogger(StandaloneClusterClientHelper.class);

    @Override
    public JobSubmitResponse submit(JobDeployHelper jobDeployer) throws Exception {

        JobOptions jobOptions = jobDeployer.getJobOptions();
        Configuration flinkConfig = FlinkEnvHelper.loadFlinkConfiguration(jobOptions);

        try (StandaloneClusterDescriptor standaloneClusterDescriptor =
                     new StandaloneClusterDescriptor(flinkConfig)) {
            ClusterClient clusterClient =
                    standaloneClusterDescriptor
                            .retrieve(StandaloneClusterId.getInstance())
                            .getClusterClient();
            JobGraph jobGraph = JobGraphHelper.buildJobGraph(jobOptions);
            JobID jobID = (JobID) clusterClient.submitJob(jobGraph).get();
            LOG.info("submit job successfully, jobID = {}", jobID);
            return new JobSubmitResponse(null != jobID ? jobID.toHexString() : "unknow");
        }
    }
}
