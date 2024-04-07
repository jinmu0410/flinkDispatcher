package com.jm.flink.lancher;


import com.jm.flink.base.CResult;
import com.jm.flink.base.bean.EnvParams;
import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.base.bean.RunParams;
import com.jm.flink.base.classloader.ClassLoaderManager;
import com.jm.flink.base.enums.DeployMode;
import com.jm.flink.base.enums.LauncherStatus;
import com.jm.flink.base.utils.GsonUtil;
import com.jm.flink.base.utils.JobOptionParserUtil;
import com.jm.flink.base.utils.JsonUtil;
import com.jm.flink.bean.JobSubmitResponse;
import com.jm.flink.helper.*;
import com.jm.flink.intel.JobClientHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author jinmu @ClassName Launcher.java @Description TODO
 * @createTime 2022/04/29
 */
public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    /**
     * loadLog4jSetting
     *
     * @return void
     * @author jinmu
     * @created 2022/5/11
     */
    static {
        String flinkDoLogFile = System.getProperty("flinkDoLogFile");
        if (StringUtils.isNotBlank(flinkDoLogFile)) {
            //
            File logFile = new File(flinkDoLogFile);
            if (!logFile.exists()) {
                logger.error("自定义日志文件不存在");
            }
            PropertyConfigurator.configure(flinkDoLogFile);
        }
    }

    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();
        logger.info("------------FLinkDo SDK Launcher start-------------------------");
        JobOptions jobOptions = null;
        try {
            jobOptions = new JobOptionParserUtil(args, true).getJobOptions();
        } catch (Exception exception) {
            logger.error("FlinkRun job params parse error: {}", exception.getMessage(), exception);
            System.exit(LauncherStatus.PARAM_ERROR.getCode());
            return;
        }
        if (null == jobOptions) {
            logger.error("FlinkRun job error: jobOptions is Required!");
            System.exit(LauncherStatus.PARAM_ERROR.getCode());
            return;
        }
        String jobContent =
                URLDecoder.decode(jobOptions.getJobContent(), StandardCharsets.UTF_8.name());

        if (StringUtils.isBlank(jobContent)) {
            logger.error("FlinkRun job error: jobContent is blank !");
            System.exit(LauncherStatus.PARAM_ERROR.getCode());
            return;
        }

        // 解析运行参数(程序自定义部分)
        RunParams runParams = null;
        if (StringUtils.isNotBlank(jobOptions.getRunParams())) {
            runParams = JsonUtil.toObject(jobOptions.getRunParams(), RunParams.class);
        }

        JobDeployHelper jobDeployer = new JobDeployHelper(jobOptions);

        JobClientHelper jobClientHelper = null;
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

            EnvParams envParams = FlinkEnvHelper.loadEnvParams(jobOptions);

            // 本地UDF函数环境准备(提前下载好准备给table sql使用)
            PluginHelper.downloadUdf(envParams);

            // 当前进程内ClassLoader
            /**
             * 1. Launcher 立即需要作业的Jar包需要在这里load进来;
             * 2. Core 中作业需要的Jar包需要在这里load进来;
             * 3. 没有load的class，Class.forName class not fund
             */


            ClassLoader classLoader = Launcher.class.getClassLoader();
            if (classLoader instanceof URLClassLoader) {
                URLClassLoader urlClassLoader = (URLClassLoader) Launcher.class.getClassLoader();
                // 当前ClassLoader 加载当前本地执行环境JAR(也可以通过POM文件引入相关JAR包)[主要是加载插件包内容]
                if (null != envParams && StringUtils.isNotBlank(envParams.getUserJarLib())) {
                    // 用户额外的jar包
                    List<URL> jarUrlList = PluginHelper.loadExternalURLUserDir(envParams.getUserJarLib());
                    ClassLoaderManager.loadExtraJar(jarUrlList, urlClassLoader);
                }
            }

            jobSubmitResponse = jobClientHelper.submit(jobDeployer);

            // 输出业务响应结果日志
            printJobSubmitResult(jobSubmitResponse, "");
        } catch (Exception exception) {
            logger.error("FLinkDo submit job error: [{}]", exception.getMessage(), exception);
            // 输出业务响应结果日志
            printJobSubmitResult(jobSubmitResponse, exception.getMessage());
            System.exit(LauncherStatus.EXCEPTION.getCode());
        } finally {
            logger.info("FLinkDo SDK Submit Job,Mode:{}, totalTimes:{}ms", jobOptions.getMode(), (System.currentTimeMillis() - startTime));
        }

        // try auto time out exit
        if (DeployMode.local.getName().equals(jobOptions.getMode()) && null != runParams) {
            if (null != runParams.getLaunchTimeOut() && runParams.getLaunchTimeOut() > 0L) {
                Long timeOut = runParams.getLaunchTimeOut();
                new Timer("FLinkDo-TimeOut-Exit", false).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        logger.info("FLinkDo program run timeout exit, with timeOut:{}", timeOut);
                        System.exit(LauncherStatus.TIME_OUT.getCode());
                    }
                }, timeOut);
            }
        }

        logger.info("------------FLinkDo SDK Launcher end-------------------------");

    }

    private static void printJobSubmitResult(JobSubmitResponse jobSubmitResponse, String errorMsg) {
        if (null == jobSubmitResponse || null == jobSubmitResponse.getJobId()) {
            logger.info("FlinkDo-ACK:{}", JsonUtil.toJson(CResult.buildErrorResult(errorMsg, jobSubmitResponse)));
        } else {
            logger.info("FlinkDo-ACK:{}", JsonUtil.toJson(CResult.buildSuccessResult(jobSubmitResponse)));
        }
    }

}
