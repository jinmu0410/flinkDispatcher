package com.jm.flink.helper;

import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.base.exception.FlinkDoRuntimeException;
import com.jm.flink.bean.JobSubmitResponse;
import com.jm.flink.intel.AbstractYarnJobClientHelper;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * YarnSessionClusterJob
 *
 * @author tasher
 * @created 2022/4/13
 */
public class YarnSessionClusterJobClientHelper extends AbstractYarnJobClientHelper {

    private static final Logger LOG =
            LoggerFactory.getLogger(YarnSessionClusterJobClientHelper.class);

    @SuppressWarnings("rawtypes")
    @Override
    public JobSubmitResponse submit(JobDeployHelper jobDeployer) throws IOException {
        try {
            JobOptions jobOptions = jobDeployer.getJobOptions();
            ClusterSpecification clusterSpecification = createClusterSpecification(jobOptions);
            YarnClusterDescriptor yarnClusterDescriptor = FlinkEnvHelper.createYarnClusterDescriptor(jobOptions);
            LOG.info("yarn session create yarnClusterDescriptor successfully");

            ClusterClientProvider<ApplicationId> clusterClientProvider = yarnClusterDescriptor.deploySessionCluster(clusterSpecification);
            if (clusterClientProvider != null && clusterClientProvider.getClusterClient().getClusterId() != null) {
                LOG.info("yarn session buildJobGraph start,waiting for submit JOB");
                ClusterClient<ApplicationId> clusterClient = clusterClientProvider.getClusterClient();
                JobGraph jobGraph = JobGraphHelper.buildJobGraph(jobOptions);
                JobID jobId = clusterClient.submitJob(jobGraph).get();
                LOG.info("submit job successfully,clusterId={}, jobID = {}", clusterClient.getClusterId(), jobId);
                JobSubmitResponse jobSubmitResponse = new JobSubmitResponse(null != jobId ? jobId.toHexString() : "unknow");
                jobSubmitResponse.setYarnApplicationId(String.valueOf(clusterClient.getClusterId()));
                jobSubmitResponse.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
                return jobSubmitResponse;
            }
            return new JobSubmitResponse(null);
        } catch (Exception e) {
            LOG.error("submit job error,{}", e.getMessage(), e);
            throw new FlinkDoRuntimeException(e);
        }
    }
}
