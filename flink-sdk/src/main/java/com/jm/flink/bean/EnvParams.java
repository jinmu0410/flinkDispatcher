package com.jm.flink.bean;




import com.jm.flink.utils.OptionRequired;

import java.io.Serializable;

/**
 * @author jinmu @ClassName EnvParams.java @Description 环境运行参数配置(主要设计大数据底层相关中间件环境配置)
 */
public class EnvParams implements Serializable {

    private static final long serialVersionUID = -9194052743337224732L;

    /**
     * flink jar包目录(可以是hdfs)
     */
    @OptionRequired(required = true, description = "setting for flink libs")
    private String flinkLibDir;

    /**
     * flink conf包目录(当前支持本地目录)
     */
    private String flinkConfDir;

    /**
     * flink 动态配置内容(map struct)
     */
    private String flinkDynamicConf;

    /**
     * hadoop conf目录
     */
    private String hadoopConfDir;

    /**
     * hadoop user(default root user)
     */
    private String hadoopUser = "root";

    /**
     * 远程插件/udf函数库存储配置(todo 暂时实现HDFS存储获取函数库)
     */
    private String remoteStorageConf = "{}";

    /**
     * 提交YARN资源队列
     */
    private String yarnQueue = "default";

    /**
     * 解决HADOOP/FLINK使用host name 无法通信问题 json map结构,host:ip,{"host_1":"192.168.100.1"}
     */
    private String yarnHosts = "{}";

    /**
     * FLINK 任务隔离设计(本地UDF函数库隔离)
     */
    private String userIdentify;

    /**
     * 插件加载方式(主要涉及JAR的load)
     */
    private String pluginMode = "classpath";

    @OptionRequired(
            required = false,
            description = "attach others jar in job,split with';',like 'a;b;c;'")
    private String shipJars;

    @OptionRequired(required = false, description = "attach others user jar in job")
    private String userJarLib;

    @OptionRequired(required = false, description = "attach local udf jar in job")
    private String udfJars;

    @OptionRequired(required = true, description = "this plugin jar list dir")
    private String pluginPath;

    @OptionRequired(required = false, description = "for standalone mode")
    private String jobManagerRpcAddress = "127.0.0.1";

    public String getFlinkLibDir() {
        return flinkLibDir;
    }

    public void setFlinkLibDir(String flinkLibDir) {
        this.flinkLibDir = flinkLibDir;
    }

    public String getFlinkConfDir() {
        return flinkConfDir;
    }

    public void setFlinkConfDir(String flinkConfDir) {
        this.flinkConfDir = flinkConfDir;
    }

    public String getFlinkDynamicConf() {
        return flinkDynamicConf;
    }

    public void setFlinkDynamicConf(String flinkDynamicConf) {
        this.flinkDynamicConf = flinkDynamicConf;
    }

    public String getHadoopConfDir() {
        return hadoopConfDir;
    }

    public void setHadoopConfDir(String hadoopConfDir) {
        this.hadoopConfDir = hadoopConfDir;
    }

    public String getHadoopUser() {
        return hadoopUser;
    }

    public void setHadoopUser(String hadoopUser) {
        this.hadoopUser = hadoopUser;
    }

    public String getPluginMode() {
        return pluginMode;
    }

    public void setPluginMode(String pluginMode) {
        this.pluginMode = pluginMode;
    }

    public String getShipJars() {
        return shipJars;
    }

    public void setShipJars(String shipJars) {
        this.shipJars = shipJars;
    }

    public String getUserJarLib() {
        return userJarLib;
    }

    public void setUserJarLib(String userJarLib) {
        this.userJarLib = userJarLib;
    }

    public String getYarnQueue() {
        return yarnQueue;
    }

    public void setYarnQueue(String yarnQueue) {
        this.yarnQueue = yarnQueue;
    }

    public String getYarnHosts() {
        return yarnHosts;
    }

    public void setYarnHosts(String yarnHosts) {
        this.yarnHosts = yarnHosts;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
    }

    public String getRemoteStorageConf() {
        return remoteStorageConf;
    }

    public void setRemoteStorageConf(String remoteStorageConf) {
        this.remoteStorageConf = remoteStorageConf;
    }

    public String getUserIdentify() {
        return userIdentify;
    }

    public void setUserIdentify(String userIdentify) {
        this.userIdentify = userIdentify;
    }

    public String getUdfJars() {
        return udfJars;
    }

    public void setUdfJars(String udfJars) {
        this.udfJars = udfJars;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void setJobManagerRpcAddress(String jobManagerRpcAddress) {
        this.jobManagerRpcAddress = jobManagerRpcAddress;
    }

    public String getJobManagerRpcAddress() {
        return jobManagerRpcAddress;
    }
}
