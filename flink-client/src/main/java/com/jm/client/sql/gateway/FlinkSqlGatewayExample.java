package com.jm.client.sql.gateway;


import com.jm.client.sql.gateway.api.DefaultApi;
import com.jm.client.sql.gateway.invoker.ApiException;
import com.jm.client.sql.gateway.model.*;
import com.jm.client.sql.gateway.model.*;

import java.util.UUID;

public class FlinkSqlGatewayExample {

    private static final String YARN_APP_ID = "application_1709610983537_0086";
    private static final String FLINK_SQL_GATEWAY = "http://192.168.201.75:8083";
    private FlinkSqlGatewayExample() {}

    public static void main(String[] args) throws ApiException {
        DefaultApi api = FlinkSqlGateway.sqlGatewayApi(FLINK_SQL_GATEWAY);
        // run on YARN
        runOnYarn(api);

        // run on K8S
        runOnKubernetes(api);



        // run with UDF
        runOnYarnWithUDF(api);

        // run stop job
        stopJob(api);
    }

    /**
     * run on kubernetes
     *
     * @param api
     * @throws ApiException
     */
    private static void runOnKubernetes(DefaultApi api) throws ApiException {
        OpenSessionResponseBody response = api.openSession(new OpenSessionRequestBody()
                .putPropertiesItem("execution.target", "kubernetes-session")
                .putPropertiesItem("jobmanager", "127.0.0.1:8081")
                .putPropertiesItem("kubernetes.cluster-id", "custom-flink-cluster")
                .putPropertiesItem("kubernetes.jobmanager.service-account", "flink")
                .putPropertiesItem("kubernetes.namespace", "flink-cluster")
                .putPropertiesItem("rest.address", "127.0.0.1")
                .putPropertiesItem("rest.port", "8081"));

        System.out.println(response.getSessionHandle());

        ExecuteStatementResponseBody statement1 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("CREATE TABLE datagen (\n" + " f_sequence INT,\n"
                                + " f_random INT,\n"
                                + " f_random_str STRING\n"
                                + ") WITH (\n"
                                + " 'connector' = 'datagen',\n"
                                + " 'rows-per-second'='10',\n"
                                + " 'fields.f_sequence.kind'='sequence',\n"
                                + " 'fields.f_sequence.start'='1',\n"
                                + " 'fields.f_sequence.end'='1000',\n"
                                + " 'fields.f_random.min'='1',\n"
                                + " 'fields.f_random.max'='1000',\n"
                                + " 'fields.f_random_str.length'='10'\n"
                                + ")")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on K8S Example"));

        System.out.println(statement1.getOperationHandle());

        ExecuteStatementResponseBody statement2 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("CREATE TABLE blackhole_table  (\n" + " f_sequence INT,\n"
                                + " f_random INT,\n"
                                + " f_random_str STRING\n"
                                + ") WITH (\n"
                                + " 'connector' = 'blackhole'\n"
                                + ")")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on K8S Example"));

        System.out.println(statement2.getOperationHandle());

        ExecuteStatementResponseBody statement3 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("CREATE TABLE print_table  (\n" + " f_sequence INT,\n"
                                + " f_random INT,\n"
                                + " f_random_str STRING\n"
                                + ") WITH (\n"
                                + " 'connector' = 'print'\n"
                                + ")")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on K8S Example"));

        System.out.println(statement3.getOperationHandle());

        ExecuteStatementResponseBody statement4 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("EXECUTE STATEMENT SET\n" + "BEGIN\n"
                                + "    insert into blackhole_table select * from datagen;\n"
                                + "    insert into print_table select * from datagen;\n"
                                + "END;")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on K8S Example"));

        System.out.println(statement4.getOperationHandle());
    }

    /**
     * run on YARN
     *
     * @param api
     * @throws ApiException
     */
    public static void runOnYarn(DefaultApi api) throws ApiException {
        OpenSessionResponseBody response = api.openSession(new OpenSessionRequestBody()
                .putPropertiesItem("execution.target", "yarn-session")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.enabled", "true")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.rm-ids", "rm1,rm2")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm1", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm2", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.cluster-id", "yarn-cluster")
                .putPropertiesItem(
                        "flink.hadoop.yarn.client.failover-proxy-provider",
                        "org.apache.hadoop.yarn.client.ConfiguredRMFailoverProxyProvider")
                .putPropertiesItem("yarn.application.id", YARN_APP_ID));
        System.out.println(response.getSessionHandle());

        ExecuteStatementResponseBody executeStatementResponseBody = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("select 1")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on YARN Example"));
        System.out.println(executeStatementResponseBody.getOperationHandle());

        ExecuteStatementResponseBody createSourceTableStatement = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("CREATE TABLE datagen (\n" + " f_sequence INT,\n"
                                + " f_random INT,\n"
                                + " f_random_str STRING\n"
                                + ") WITH (\n"
                                + " 'connector' = 'datagen',\n"
                                + " 'rows-per-second'='10',\n"
                                + " 'fields.f_sequence.kind'='sequence',\n"
                                + " 'fields.f_sequence.start'='1',\n"
                                + " 'fields.f_sequence.end'='1000',\n"
                                + " 'fields.f_random.min'='1',\n"
                                + " 'fields.f_random.max'='1000',\n"
                                + " 'fields.f_random_str.length'='10'\n"
                                + ")")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on K8S Example"));

        System.out.println(createSourceTableStatement.getOperationHandle());

        // create source table
        ExecuteStatementResponseBody createSinkTableStatement_1 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("CREATE TABLE blackhole_table  (\n" + " f_sequence INT,\n"
                                + " f_random INT,\n"
                                + " f_random_str STRING\n"
                                + ") WITH (\n"
                                + " 'connector' = 'blackhole'\n"
                                + ")")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on YARN Example"));

        System.out.println(createSinkTableStatement_1.getOperationHandle());

        ExecuteStatementResponseBody createSinkTableStatement_2 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("CREATE TABLE print_table  (\n" + " f_sequence INT,\n"
                                + " f_random INT,\n"
                                + " f_random_str STRING\n"
                                + ") WITH (\n"
                                + " 'connector' = 'print'\n"
                                + ")")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on YARN Example"));

        System.out.println(createSinkTableStatement_2.getOperationHandle());

        ExecuteStatementResponseBody executeSetStatement = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("EXECUTE STATEMENT SET\n" + "BEGIN\n"
                                + "    insert into blackhole_table select * from datagen;\n"
                                + "    insert into print_table select * from datagen;\n"
                                + "END;")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on YARN Example"));

        System.out.println(executeSetStatement.getOperationHandle());


        ExecuteStatementResponseBody queryStatement = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("select * from datagen;")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on YARN Example"));

        System.out.println(queryStatement.getOperationHandle());


        FetchResultsResponseBody fetchResultsResponseBody = api.fetchResults(UUID.fromString(response.getSessionHandle()),
                UUID.fromString(queryStatement.getOperationHandle()),
                Long.parseLong(response.getSessionHandle()),
                RowFormat.JSON);

        System.out.println(fetchResultsResponseBody.toJson());

    }


    /**
     * run on YARN with UDF
     *
     * @param api
     * @throws ApiException
     */
    public static void runOnYarnWithUDF(DefaultApi api) throws ApiException {
        OpenSessionResponseBody response = api.openSession(new OpenSessionRequestBody()
                .putPropertiesItem("execution.target", "yarn-session")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.enabled", "true")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.rm-ids", "rm1,rm2")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm1", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm2", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.cluster-id", "yarn-cluster")
                .putPropertiesItem(
                        "flink.hadoop.yarn.client.failover-proxy-provider",
                        "org.apache.hadoop.yarn.client.ConfiguredRMFailoverProxyProvider")
                .putPropertiesItem("yarn.application.id", YARN_APP_ID));

        ExecuteStatementResponseBody statment1 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("create TEMPORARY FUNCTION \n"
                                + "    FakeFunction as 'com.flink.udf.FakeFunction'\n"
                                + "using JAR 'hdfs://.../udf-test/fake-func.jar'")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway UDF on YARN Example"));
        System.out.println(statment1.getOperationHandle());

        ExecuteStatementResponseBody statment2 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("select FakeFunction('Flink SQL Gateway UDF on YARN Example')")
                        .putExecutionConfigItem(
                                "pipeline.name", "Flink SQL Gateway UDF on YARN Example-" + UUID.randomUUID()));
        System.out.println(statment2.getOperationHandle());
    }

    /**
     * stop job
     *
     * @since Flink 1.17
     * @param api
     * @throws ApiException
     */
    private static void stopJob(DefaultApi api) throws ApiException {
        OpenSessionResponseBody response = api.openSession(new OpenSessionRequestBody()
                .putPropertiesItem("execution.target", "yarn-session")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.enabled", "true")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.rm-ids", "rm1,rm2")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm1", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm2", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.cluster-id", "yarn-cluster")
                .putPropertiesItem(
                        "flink.hadoop.yarn.client.failover-proxy-provider",
                        "org.apache.hadoop.yarn.client.ConfiguredRMFailoverProxyProvider")
                .putPropertiesItem("yarn.application.id", YARN_APP_ID));
        System.out.println(response.getSessionHandle());

        ExecuteStatementResponseBody executeStatementResponseBody = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .putExecutionConfigItem("state.savepoints.dir", "hdfs://.../savepoints")
                        .statement("stop job '89ea3d3410cc8fa8c8c5bb44fc6e8a7f' with SAVEPOINT"));

        System.out.println(executeStatementResponseBody.getOperationHandle());
    }
}
