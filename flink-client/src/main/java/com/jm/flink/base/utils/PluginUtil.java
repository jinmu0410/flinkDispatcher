/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jm.flink.base.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jm.flink.base.bean.EnvParams;
import com.jm.flink.base.conf.MetricPluginConf;
import com.jm.flink.base.conf.SyncJobConf;
import com.jm.flink.base.constants.ConstantValue;
import com.jm.flink.base.enums.DeployMode;
import com.jm.flink.base.exception.FlinkDoRuntimeException;
import com.jm.flink.constant.FlinkDoConstants;
import com.jm.flink.environment.FlinkDoLocalStreamEnvironment;
import com.jm.flink.function.FunctionDefBean;
import com.jm.flink.sql.enums.OperatorType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.cache.DistributedCache;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Reason: Date: 2018/6/27 Company:
 *
 * @author
 */
public class PluginUtil {

    private static final Logger logger = LoggerFactory.getLogger(PluginUtil.class);

    public static final String FORMATS_SUFFIX = "formats";

    public static final String READER_SUFFIX = "reader";
    private static final String JAR_SUFFIX = ".jar";
    public static final String SOURCE_SUFFIX = "source";
    public static final String WRITER_SUFFIX = "writer";
    public static final String SINK_SUFFIX = "sink";
    public static final String GENERIC_SUFFIX = "Factory";

    private static final String SP = File.separator;
    private static final Logger LOG = LoggerFactory.getLogger(PluginUtil.class);
    private static final String PACKAGE_PREFIX = "com.hk.szyc.flinkdo.sql.connector.";
    private static final String METRIC_PACKAGE_PREFIX = "com.hk.szyc.flinkdo.metrics.";
    private static final String METRIC_REPORT_PREFIX = "Report";

    private static final String FILE_PREFIX = "file:";


    /**
     * 根据插件名称查找插件路径(主要: DataSyncFactoryUtil)
     *
     * @param pluginName       插件名称，如: kafkareader、kafkasource等
     * @param pluginRoot
     * @param remotePluginPath #(当前字段主要废弃)
     * @return
     */
    public static Set<URL> getJarFileDirPath(
            String pluginName, String pluginRoot, String remotePluginPath) {
        Set<URL> urlList = new HashSet<>();

        String pluginPath = Objects.isNull(remotePluginPath) ? pluginRoot : remotePluginPath;
        String name =
                pluginName
                        .replace(READER_SUFFIX, "")
                        .replace(SOURCE_SUFFIX, "")
                        .replace(WRITER_SUFFIX, "")
                        .replace(SINK_SUFFIX, "");

        try {
            String pluginJarPath = pluginRoot + SP + name;
            // 获取jar包名字，构建对应的URL地址
            for (String jarName : getJarNames(new File(pluginJarPath))) {
                urlList.add(new URL(FILE_PREFIX + pluginPath + SP + name + SP + jarName));
            }
            return urlList;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * getJarFileDirPath
     *
     * @param pluginRoot
     * @return java.util.Set<java.net.URL>
     * @author tasher
     * @param: pluginName
     * @created 2022/4/24
     */
    public static Set<URL> getJarFileDirPath(String pluginName, String pluginRoot) {
        Set<URL> urlList = new HashSet<>();

        String name =
                pluginName
                        .replace(READER_SUFFIX, "")
                        .replace(SOURCE_SUFFIX, "")
                        .replace(WRITER_SUFFIX, "")
                        .replace(SINK_SUFFIX, "");
        try {
            String pluginJarPath = pluginRoot + SP + name;
            // 获取jar包名字，构建对应的URL地址
            for (String jarName : getJarNames(new File(pluginJarPath))) {
                urlList.add(new URL(FILE_PREFIX + pluginRoot + SP + name + SP + jarName));
            }
            return urlList;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取路径当前层级下所有jar包名称
     *
     * @param pluginPath
     * @return
     */
    private static List<String> getJarNames(File pluginPath) {
        List<String> jarNames = new ArrayList<>();
        if (pluginPath.exists() && pluginPath.isDirectory()) {
            File[] jarFiles =
                    pluginPath.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
            if (Objects.nonNull(jarFiles) && jarFiles.length > 0) {
                Arrays.stream(jarFiles).forEach(item -> jarNames.add(item.getName()));
            }
        }
        return jarNames;
    }

    /**
     * 根据插件名称查找插件入口类
     *
     * @param pluginName   如：kafkareader
     * @param operatorType 算子类型
     * @return
     */
    public static String getPluginClassName(String pluginName, OperatorType operatorType) {
        String pluginClassName;
        switch (operatorType) {
            case metric:
                pluginClassName = appendMetricClass(pluginName);
                break;
            case source:
                String sourceName = pluginName.replace(READER_SUFFIX, SOURCE_SUFFIX);
                pluginClassName = camelize(sourceName, SOURCE_SUFFIX);
                break;
            case sink:
                String sinkName = pluginName.replace(WRITER_SUFFIX, SINK_SUFFIX);
                pluginClassName = camelize(sinkName, SINK_SUFFIX);
                break;
            default:
                throw new FlinkDoRuntimeException("unknown operatorType: " + operatorType);
        }

        return pluginClassName;
    }

    /**
     * 拼接插件包类全限定名
     *
     * @param pluginName 插件包名称，如：binlogsource
     * @param suffix     插件类型前缀，如：source、sink
     * @return 插件包类全限定名，如：com.xxx.connectors.binlog.source.BinlogSourceFactory
     */
    private static String camelize(String pluginName, String suffix) {
        int pos = pluginName.indexOf(suffix);
        String left = pluginName.substring(0, pos);
        left = left.toLowerCase();
        suffix = suffix.toLowerCase();
        StringBuilder sb = new StringBuilder(32);
        sb.append(PACKAGE_PREFIX);
        sb.append(ConnectorNameConvertUtil.convertPackageName(left))
                .append(ConstantValue.POINT_SYMBOL)
                .append(suffix)
                .append(ConstantValue.POINT_SYMBOL);
        left = ConnectorNameConvertUtil.convertClassPrefix(left);
        sb.append(left.substring(0, 1).toUpperCase()).append(left.substring(1));
        sb.append(suffix.substring(0, 1).toUpperCase()).append(suffix.substring(1));
        sb.append(GENERIC_SUFFIX);
        return sb.toString();
    }

    private static String appendMetricClass(String pluginName) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(METRIC_PACKAGE_PREFIX)
                .append(pluginName.toLowerCase(Locale.ENGLISH))
                .append(ConstantValue.POINT_SYMBOL);
        sb.append(pluginName.substring(0, 1).toUpperCase())
                .append(pluginName.substring(1).toLowerCase());
        sb.append(METRIC_REPORT_PREFIX);
        return sb.toString();
    }

    /**
     * 将任务所用到的插件包注册到env中
     *
     * @param envParams
     * @return void
     * @author tasher
     * @param: env
     * @created 2022/4/24
     */
    public static void registerJobPluginToCachedFile(
            StreamExecutionEnvironment env,
            EnvParams envParams,
            SyncJobConf syncJobConf,
            MetricPluginConf metricPluginConf) {

        Set<URL> urlSet = new HashSet<>();

        // core
        Set<URL> coreUrlList = getJarFileDirPath("", envParams.getPluginPath());

        Set<URL> formatsUrlList =
                getJarFileDirPath(FORMATS_SUFFIX, envParams.getPluginPath(), null);

        Set<URL> sourceUrlList =
                getJarFileDirPath(
                        syncJobConf.getReader().getName(),
                        envParams.getPluginPath() + SP + ConstantValue.CONNECTOR_DIR_NAME,
                        null);
        Set<URL> sinkUrlList =
                getJarFileDirPath(
                        syncJobConf.getWriter().getName(),
                        envParams.getPluginPath() + SP + ConstantValue.CONNECTOR_DIR_NAME,
                        null);
        Set<URL> metricUrlList =
                getJarFileDirPath(
                        metricPluginConf.getPluginName(),
                        envParams.getPluginPath() + SP + ConstantValue.METRIC_DIR_NAME,
                        null);
        // 核心包
        urlSet.addAll(coreUrlList);
        // formats
        urlSet.addAll(formatsUrlList);
        // source
        urlSet.addAll(sourceUrlList);
        // sink
        urlSet.addAll(sinkUrlList);
        // 监控
        urlSet.addAll(metricUrlList);

        for (URL url : urlSet) {
            String index = Md5Util.getMd5(url.getPath());
            String classFileName = String.format(FlinkDoConstants.CLASS_FILE_NAME_FMT_STR, index);
            env.registerCachedFile(url.getPath(), classFileName, true);
        }
        if (env instanceof FlinkDoLocalStreamEnvironment) {
            localClassLoader(urlSet);
        }
    }

    /**
     * register shipfile to StreamExecutionEnvironment cachedFile
     *
     * @param shipfile the shipfile which needed to add into cacheFile
     * @param env      StreamExecutionEnvironment
     */
    public static void registerShipfileToCachedFile(
            String shipfile, StreamExecutionEnvironment env) {
        if (StringUtils.isNotBlank(shipfile)) {
            String[] files = shipfile.split(ConstantValue.COMMA_SYMBOL);
            Set<String> fileNameSet = new HashSet<>(8);
            for (String filePath : files) {
                String fileName = new File(filePath).getName();
                if (fileNameSet.contains(fileName)) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "can not add duplicate fileName to cachedFiles, duplicate fileName = %s, shipfile = %s",
                                    fileName, shipfile));
                } else if (!new File(filePath).exists()) {
                    throw new IllegalArgumentException(
                            String.format("file: [%s] is not exists", filePath));
                } else {
                    env.registerCachedFile(filePath, fileName, false);
                    fileNameSet.add(fileName);
                }
            }
            fileNameSet.clear();
        }
    }

    /**
     * 当前配置内容中所有jar包名称
     *
     * <p>例如: a.jar;b.jar;c.jar
     *
     * @param shipJars
     * @return java.util.List<java.lang.String>
     * @author tasher
     * @created 2022/4/11
     */
    public static Set<URL> getShipJars(String shipJars) throws MalformedURLException {
        if (StringUtils.isBlank(shipJars)) {
            return Sets.newHashSet();
        }
        String[] jars = shipJars.split(";");
        if (jars.length <= 0) {
            return Sets.newHashSet();
        }

        Set<URL> jarNames = Sets.newHashSet();
        for (String jar : jars) {
            if (jar.length() <= 1 || !jar.endsWith(".jar")) {
                continue;
            }
            // 这里是否要校验文件路径是否存在，待验证
            jarNames.add(new URL(jar));
        }
        return jarNames;
    }

    /**
     * getUdfJarsByLib
     *
     * @param pluginRoot
     * @return java.util.Set<java.net.URL>
     * @author tasher
     * @created 2022/4/13
     */
    @SuppressWarnings("DuplicatedCode")
    public static List<FunctionDefBean> getUdfJarsByLib(String pluginRoot, String identify) {
        if (StringUtils.isBlank(pluginRoot)) {
            return Lists.newArrayList();
        }
        if (StringUtils.isBlank(identify)) {
            identify = "";
        }
        File functionDir = new File(pluginRoot + SP + ConstantValue.FUNCTION_DIR_NAME + SP + Md5Util.getMd5(identify).toUpperCase());
        if (functionDir.exists() && functionDir.isDirectory()) {
            List<FunctionDefBean> functionDefBeans = Lists.newArrayList();
            File[] jarFiles =
                    functionDir.listFiles((dir, file) -> file.toLowerCase().endsWith(".mtd"));
            if (Objects.nonNull(jarFiles) && jarFiles.length > 0) {
                for (File funFile : jarFiles) {
                    // 查找元数据
                    try {
                        byte[] unMtd = FileUtils.readFileToByteArray(funFile);
                        String metaStr = new String(unMtd, StandardCharsets.UTF_8);
                        logger.info("download meta string:{}", metaStr);
                        if (StringUtils.isBlank(metaStr)) {
                            logger.error("meta信息有误!");
                            continue;
                        }
                        List<String> metaInfoList = Lists.newArrayList(metaStr.split(","));
                        if (CollectionUtils.isEmpty(metaInfoList) || metaInfoList.size() < 4) {
                            logger.error("meta信息不完整!");
                            continue;
                        }

                        // local file
                        FunctionDefBean functionDefBean = new FunctionDefBean();
                        functionDefBean.setFunctionName(metaInfoList.get(0));
                        functionDefBean.setFunctionClass(metaInfoList.get(1));
                        functionDefBean.setFunctionLocation(metaInfoList.get(2));
                        functionDefBean.setFunctionFileMd5(metaInfoList.get(3));
                        functionDefBean.setLocalJarUrl(funFile.getParent() + File.separator + funFile.getName().substring(0, funFile.getName().lastIndexOf(".")) + ".jar");
                        functionDefBeans.add(functionDefBean);
                    } catch (IOException ioException) {
                        logger.error("read udf mtd file error:{}", ioException.getMessage(), ioException);
                    }
                }

            }
            return functionDefBeans;
        }
        return Lists.newArrayList();
    }

    /**
     * 这里主要给shipJar/userLib使用
     *
     * <p>Job依赖的插件包路径存储到cacheFile，在外围将插件包路径传递给jobGraph
     *
     * @param classPathSet
     * @return void
     * @author tasher
     * @param: env
     * @created 2022/4/11
     */
    public static void registerPluginUrlToCachedFile(
            StreamExecutionEnvironment env, Set<URL> classPathSet) {
        Set<String> fileNameSet = new HashSet<>(8);
        for (URL url : classPathSet) {
            String fileIndex = Md5Util.getMd5(url.getPath());
            String classFileName = String.format(FlinkDoConstants.CLASS_FILE_NAME_FMT_STR, fileIndex);
            if (!fileNameSet.contains(classFileName)) {
                env.registerCachedFile(url.getPath(), classFileName, false);
                fileNameSet.add(classFileName);
            }
        }
        // load classpath 下JAR包类方法
        if (env instanceof FlinkDoLocalStreamEnvironment) {
            localClassLoader(classPathSet);
        }
        fileNameSet.clear();
    }

    public static List<String> setPipelineOptionsToEnvConfig(
            StreamExecutionEnvironment env, List<String> urlList, String executionMode) {
        try {
            Configuration configuration =
                    (Configuration)
                            ReflectionUtils.getDeclaredMethod(env, "getConfiguration").invoke(env);
            List<String> jarList = configuration.get(PipelineOptions.JARS);
            if (jarList == null) {
                jarList = new ArrayList<>(urlList.size());
            }
            jarList.addAll(urlList);

            List<String> pipelineJars = new ArrayList<String>();
            LOG.debug("FlinkDo executionMode: {}", executionMode);
            if (DeployMode.getByName(executionMode).equals(DeployMode.kubernetesApplication)) {
                for (String jarUrl : jarList) {
                    String newJarUrl = jarUrl;
                    if (StringUtils.startsWith(jarUrl, File.separator)) {
                        newJarUrl = "file:" + jarUrl;
                    }
                    if (pipelineJars.contains(newJarUrl)) {
                        continue;
                    }
                    pipelineJars.add(newJarUrl);
                }
            } else {
                pipelineJars.addAll(jarList);
            }

            LOG.debug("FlinkDo reset pipeline.jars: {}", pipelineJars);
            configuration.set(PipelineOptions.JARS, pipelineJars);

            List<String> classpathList = configuration.get(PipelineOptions.CLASSPATHS);
            if (classpathList == null) {
                classpathList = new ArrayList<>(urlList.size());
            }
            classpathList.addAll(pipelineJars);
            configuration.set(PipelineOptions.CLASSPATHS, classpathList);
            return pipelineJars;
        } catch (Exception e) {
            throw new FlinkDoRuntimeException(e);
        }
    }

    /**
     * Create DistributedCache from the URL of the ContextClassLoader
     *
     * @return
     */
    public static DistributedCache createDistributedCacheFromContextClassLoader() {
        Map<String, Future<Path>> distributeCachedFiles = new HashMap<>();

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader instanceof URLClassLoader) {
            URLClassLoader classLoader = (URLClassLoader) contextClassLoader;
            URL[] urLs = classLoader.getURLs();

            for (URL url : urLs) {
                String path = url.getPath();
                String name =
                        path.substring(path.lastIndexOf(ConstantValue.SINGLE_SLASH_SYMBOL) + 1);
                distributeCachedFiles.put(name, CompletableFuture.completedFuture(new Path(path)));
            }
            return new DistributedCache(distributeCachedFiles);
        } else {
            LOG.warn("ClassLoader: {} is not instanceof URLClassLoader", contextClassLoader);
            return null;
        }
    }


    /**
     * getUserJarsByLib
     *
     * @param libPath
     * @return java.util.Set<java.net.URL>
     * @author tasher
     * @created 2022/4/13
     */
    public static Set<URL> getUserJarsByLib(String libPath) {
        if (StringUtils.isBlank(libPath)) {
            return Sets.newHashSet();
        }
        File tempFile = new File(libPath);
        if (tempFile.exists() && tempFile.isDirectory()) {
            Set<URL> jarNames = Sets.newHashSet();
            File[] jarFiles =
                    tempFile.listFiles((dir, file) -> file.toLowerCase().endsWith(".jar"));
            if (Objects.nonNull(jarFiles) && jarFiles.length > 0) {
                Arrays.stream(jarFiles)
                        .forEach(
                                item -> {
                                    try {
                                        jarNames.add(item.toURI().toURL());
                                    } catch (MalformedURLException e) {
                                        LOG.error("getJarNames error:{}", e.getMessage(), e);
                                    }
                                });
            }
            return jarNames;
        }

        return Sets.newHashSet();
    }

    /**
     * localClassLoader
     *
     * @param urlSet
     * @return void
     * @author tasher
     * @created 2022/4/24
     */
    private static void localClassLoader(Set<URL> urlSet) {
        // load classpath 下JAR包类方法
        if (CollectionUtils.isNotEmpty(urlSet)) {
            try {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                add.setAccessible(true);
                add.invoke(contextClassLoader, new ArrayList<>(urlSet).get(0));
            } catch (Exception e) {
                LOG.warn(
                        "cannot add some jars into contextClassLoader, urlSet = {}",
                        JsonUtil.toJson(urlSet),
                        e);
            }
        }
    }
}
