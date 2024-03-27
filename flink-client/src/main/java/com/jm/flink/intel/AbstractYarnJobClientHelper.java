package com.jm.flink.intel;

import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.base.utils.JobOptionParserUtil;
import com.jm.flink.base.utils.JsonUtil;
import com.jm.flink.base.utils.ValueUtil;
import com.jm.flink.constant.FlinkDoPluginConstants;
import com.jm.flink.helper.FlinkEnvHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.configuration.Configuration;

import java.io.IOException;

import static com.jm.flink.constant.FlinkDoConstants.MAIN_CLASS;

public abstract class AbstractYarnJobClientHelper implements JobClientHelper {

    protected ClusterSpecification createClusterSpecification(JobOptions jobOptions) {
//        Configuration flinkConfiguration = FlinkEnvHelper.loadFlinkConfiguration(jobOptions);
//        int jobManagerMemoryMb = 1024;
//        int taskManagerMemoryMb = 1024;
//        int slotsPerTaskManager = FlinkDoPluginConstants.TM_NUM_TASK_SLOTS.defaultValue();
//
//        // jobmanager.memory.process.size
//        if(flinkConfiguration.containsKey(FlinkDoPluginConstants.JM_MEM_PROCESS_MEMORY.key())) {
//            Integer tmpVal = ValueUtil.parserStr(flinkConfiguration.getString(FlinkDoPluginConstants.JM_MEM_PROCESS_MEMORY));
//            if(null != tmpVal){
//                jobManagerMemoryMb = tmpVal;
//            }
//        }
//        // taskmanager.memory.process.size taskmanager.memory.framework.heap.size
//        if(flinkConfiguration.containsKey(FlinkDoPluginConstants.TM_MEM_PROCESS_MEMORY.key())) {
//            Integer tmpVal = ValueUtil.parserStr(flinkConfiguration.getString(FlinkDoPluginConstants.TM_MEM_PROCESS_MEMORY));
//            if(null != tmpVal){
//                taskManagerMemoryMb = tmpVal;
//            }
//        }
//        // taskmanager.numberOfTaskSlots
//        if (flinkConfiguration.containsKey(FlinkDoPluginConstants.TM_NUM_TASK_SLOTS.key())) {
//            slotsPerTaskManager = flinkConfiguration.getInteger(FlinkDoPluginConstants.TM_NUM_TASK_SLOTS);
//        }
        ClusterSpecification clusterSpecification =
                new ClusterSpecification.ClusterSpecificationBuilder()
                        .setMasterMemoryMB(1024)
                        .setTaskManagerMemoryMB(1024)
                        .setSlotsPerTaskManager(1)
                        .createClusterSpecification();

        return clusterSpecification;
    }

    protected ApplicationConfiguration createApplicationConfiguration(JobOptions jobOptions) {
        try {
            String[] args = JobOptionParserUtil.transformArgsFromSortFieldArray(jobOptions);
            return new ApplicationConfiguration(args, MAIN_CLASS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
