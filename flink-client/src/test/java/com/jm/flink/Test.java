package com.jm.flink;

import com.jm.flink.base.CResult;
import com.jm.flink.base.bean.EnvParams;
import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.base.bean.JobParams;
import com.jm.flink.base.enums.DeployMode;
import com.jm.flink.base.utils.JobOptionParserUtil;
import com.jm.flink.base.utils.JsonUtil;
import com.jm.flink.bean.JobSubmitResponse;
import com.jm.flink.helper.*;
import com.jm.flink.intel.JobClientHelper;
import com.jm.flink.main.Main;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws IOException {
        JobOptions jobOptions = getTestRestoreExecArgs();

        run(jobOptions);

    }

    public static void test(JobOptions jobOptions) {
        try {
            Main.main(JobOptionParserUtil.transformArgsFromSortFieldArray(jobOptions));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(JobOptions jobOptions) {
        JobClientHelper jobClientHelper = null;
        JobDeployHelper jobDeployer = new JobDeployHelper(jobOptions);
        // 提交任务结果
        JobSubmitResponse jobSubmitResponse = null;
        try {
            switch (DeployMode.getByName(jobOptions.getMode())) {
                case local:
                    // 运行本地方法使用
                    jobClientHelper = new LocalJobClientHelper();
                    break;
                case standalone:
                    jobClientHelper = new StandaloneClusterClientHelper();
                    break;
                case yarnSession:
                    jobClientHelper = new YarnSessionClusterJobClientHelper();
                    break;
                case yarnApplication:
                    jobClientHelper = new YarnApplicationClusterJobClientHelper();
                    break;
                default:
                    throw new ClusterDeploymentException(jobOptions.getMode() + " Mode not supported.");
            }
            jobSubmitResponse = jobClientHelper.submit(jobDeployer);

            printJobSubmitResult(jobSubmitResponse, "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printJobSubmitResult(JobSubmitResponse jobSubmitResponse, String errorMsg) {
        if (null == jobSubmitResponse) {
            System.out.println(("FlinkDo-ACK:{}" + JsonUtil.toJson(CResult.buildErrorResult(errorMsg, jobSubmitResponse))));
        } else {
            System.out.println(("FlinkDo-ACK:{}" + JsonUtil.toJson(CResult.buildSuccessResult(jobSubmitResponse))));
        }
    }

    public static JobOptions getTestRestoreExecArgs() {
        String flinkHdfsLib = "hdfs://doris-master:8020/dinky/flink-1.17.1/lib";
        String flinkLocalLib = "/Users/jinmu/Downloads/soft/flink-1.17.1";
        // Job 配置
        JobOptions options = new JobOptions();
        options.setJobName("test-flink-cdc-job-35");
        options.setJobMainClassJar("/Users/jinmu/Downloads/self/flinkDispatcher/flink-client/target/flink-client-1.0-SNAPSHOT.jar");

        EnvParams envParams = new EnvParams();
        // 代理flink目录
        envParams.setFlinkLibDir(flinkHdfsLib);
        // 本地配置FLINK目录(日志本地处理)
        //envParams.setFlinkConfDir("/Users/jinmu/Downloads/soft/flink-1.17.1/conf");
        envParams.setHadoopUser("root");
        envParams.setYarnQueue("default");
        envParams.setHadoopConfDir("/Users/jinmu/Downloads/hs-conf");
        envParams.setPluginMode("shipfile");
        envParams.setJobManagerRpcAddress("127.0.0.1");
        //envParams.setPluginPath("/Users/tasher/Downloads/hk_workspace/szyc-flinkdo/flinkdo-dist/plugins");


        // flink属性配置(这里需要进行一些优化配置)【当采用了HDFS flink lib 以后，那么关于FLINK的相关配置都在动态配置中】
        Properties flinkDynamicConf = new Properties();
        // 默认值就是filesystem
        flinkDynamicConf.setProperty("state.backend", "filesystem");
        // 你可以通过 state.savepoints.dir 配置 savepoint 的默认目录。 触发 savepoint 时，将使用此目录来存储 savepoint。 你可以通过使用触发器命令指定自定义目标目录来覆盖缺省值
        flinkDynamicConf.setProperty("state.checkpoints.dir", "hdfs://doris-master:8020/dinky/jinmu/flink-job/checkpoints");
        flinkDynamicConf.setProperty("state.savepoints.dir", "hdfs://doris-master:8020/dinky/jinmu/flink-job/savepoints");
        flinkDynamicConf.setProperty("sql.checkpoint.interval", "60000");
        flinkDynamicConf.setProperty("execution.savepoint.ignore-unclaimed-state", "false");
        flinkDynamicConf.setProperty("taskmanager.memory.process.size", "1024m");
        flinkDynamicConf.setProperty("jobmanager.memory.heap.size", "1024m");
        flinkDynamicConf.setProperty("jobmanager.memory.off-heap.size", "512m");
        flinkDynamicConf.setProperty("jobmanager.memory.jvm-overhead.min", "512m");
        flinkDynamicConf.setProperty("HADOOP_USER_NAME", "root");
        envParams.setFlinkDynamicConf(JsonUtil.toJson(flinkDynamicConf));

        options.setEnvParams(JsonUtil.toJson(envParams) + "");

        // job配置
        JobParams jobParams = new JobParams();
        options.setJobParams(JsonUtil.toJson(jobParams));

        String sourceSql = "CREATE TABLE IF NOT EXISTS test (\n" +
                "    `name` STRING) \n" +
                "WITH (\n" +
                "   'connector' = 'jdbc',\n" +
                "   'url' = 'jdbc:postgresql://192.168.201.54:5532/test',\n" +
                "   'table-name' = 'test',\n" +
                "   'username' = 'postgres',\n" +
                "   'password' = 'password'\n" +
                ");";

        String sinkMysql = "\n CREATE TABLE IF NOT EXISTS test_task_69 (\n" +
                "    numtest BIGINT NOT NULL, nametest STRING, dataVersion BIGINT NOT NULL ) \n" +
                "WITH (\n" +
                "   'connector' = 'doris',\n" +
                "   'jdbc-url' = 'jdbc:mysql://192.168.201.75:9030/test7',\n" +
                "   'fenodes' = '192.168.201.75:8030',\n" +
                "   'username' = 'root',\n" +
                "   'password' = '123456',\n" +
                "   'sink.enable-2pc' = 'false',\n" +
                "   'table.identifier' = 'test7.test_task_69'\n" +
                ");";

        String resultSql = "\n INSERT INTO test_task_69 select  num as numtest ,name as nametest, 1122 as dataVersion\n" +
                "from (select COUNT(name) as num, name\n" +
                "      from test\n" +
                "      group by name) t1;";

        // 具体任务
        options.setJobContent(sourceSql + sinkMysql + resultSql);
        options.setJobType("sql");
        options.setMode("standalone");
        return options;
    }
}
