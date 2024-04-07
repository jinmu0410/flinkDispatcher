package com.jm.flink.helper;


import com.jm.flink.base.bean.EnvParams;
import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.bean.JobSubmitResponse;
import com.jm.flink.intel.AbstractYarnJobClientHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.runtime.security.SecurityConfiguration;
import org.apache.flink.runtime.security.SecurityUtils;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnDeploymentTarget;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * YarnPerJobClusterJobClientHelper
 *
 * @author jinmu
 * @created 2022/4/13
 */
public class YarnApplicationClusterJobClientHelper extends AbstractYarnJobClientHelper {

    private static final Logger LOG =
            LoggerFactory.getLogger(YarnApplicationClusterJobClientHelper.class);

    @SuppressWarnings("rawtypes")
    @Override
    public JobSubmitResponse submit(JobDeployHelper jobDeployHelper) throws Exception {
        JobOptions jobOptions = jobDeployHelper.getJobOptions();
        LOG.info("start to submit application-job task, jobOptions = {}", jobOptions);

        EnvParams envParams = FlinkEnvHelper.loadEnvParams(jobOptions);

        if (null == envParams || StringUtils.isBlank(envParams.getFlinkLibDir())) {
            throw new IllegalArgumentException("per-job mode must have flink lib path!");
        }

        ClusterSpecification clusterSpecification = createClusterSpecification(jobOptions);
        YarnClusterDescriptor descriptor = FlinkEnvHelper.createYarnClusterDescriptor(jobOptions);
        LOG.info("yarn application create yarnClusterDescriptor successfully");
        ApplicationConfiguration appConfig = createApplicationConfiguration(jobOptions);

        ClusterClientProvider<ApplicationId> applicationCluster = descriptor.deployApplicationCluster(clusterSpecification, appConfig);

        ClusterClient<ApplicationId> clusterClient = applicationCluster.getClusterClient();
        if (clusterClient != null && clusterClient.getClusterId() != null) {
            String applicationId = applicationCluster.getClusterClient().getClusterId().toString();
            JobSubmitResponse jobSubmitResponse = new JobSubmitResponse();
            jobSubmitResponse.setWebInterfaceUrl(applicationCluster.getClusterClient().getWebInterfaceURL());
            jobSubmitResponse.setYarnApplicationId(applicationId);
            return jobSubmitResponse;
        }
        LOG.info("end to submit application-job task");
        return new JobSubmitResponse(null);
    }
}
