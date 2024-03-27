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

package com.jm.flink.helper;

import com.google.common.collect.Lists;
import com.jm.flink.base.bean.EnvParams;
import com.jm.flink.base.conf.RemoteStorageConf;
import com.jm.flink.base.constants.ConstantValue;
import com.jm.flink.base.enums.StorageType;
import com.jm.flink.base.utils.FileSystemUtil;
import com.jm.flink.base.utils.GsonUtil;
import com.jm.flink.base.utils.JsonUtil;
import com.jm.flink.base.utils.Md5Util;
import com.jm.flink.function.FunctionDefBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.util.MD5FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 插件帮助类
 *
 * @author tasher
 * @created 2022/4/11
 */
public class PluginHelper {

    public static final Logger logger = LoggerFactory.getLogger(PluginHelper.class);

    private static final String META_FILE_FORMAT = ".mtd";

    private static final String JAR_FILE_FORMAT = ".jar";

    /**
     * downloadUdf
     *
     * @return java.util.List<java.net.URL>
     * @author tasher
     * @created 2022/2/22
     */
    public static void downloadUdf(EnvParams envParams) {

        // parse conf
        if (null == envParams || StringUtils.isBlank(envParams.getRemoteStorageConf())) {
            return;
        }
        RemoteStorageConf remoteStorageConf = JsonUtil.toObject(envParams.getRemoteStorageConf(), RemoteStorageConf.class);
        if (null == remoteStorageConf || StringUtils.isBlank(remoteStorageConf.getStorageType())) {
            logger.warn("remoteStorageConf config error, skip load function jars.");
            return;
        }
        if (StringUtils.isBlank(remoteStorageConf.getUdfPath())) {
            return;
        }

        if (remoteStorageConf.getStorageType().equals(StorageType.HDFS.getName())) {
            // download from hdfs
            try {
                loadHdfsUdfFiles(remoteStorageConf.getStoragePros(), envParams.getPluginPath(), remoteStorageConf.getUdfPath(), envParams.getUserIdentify());
            } catch (Exception exception) {
                logger.error("load hdfs udf files error,{}", exception.getMessage(), exception);
            }
        }
    }

    private static void loadHdfsUdfFiles(Map<String, Object> hadoopConfig, String localPluginPathStr, String remoteUdfPath, String identify) throws Exception {

        if (null == hadoopConfig || StringUtils.isBlank(remoteUdfPath) || StringUtils.isBlank(localPluginPathStr)) {
            logger.warn("loadHdfsUdfFiles error, params error");
            return;
        }
        if (StringUtils.isBlank(identify)) {
            // 默认没有函数隔离
            identify = "";
        }
        // 本地缓存
        localPluginPathStr = localPluginPathStr + File.separator + ConstantValue.FUNCTION_DIR_NAME + File.separator + Md5Util.getMd5(identify).toUpperCase();

        FileSystem fileSystem = FileSystemUtil.getFileSystem(hadoopConfig);
        Path hdfsPath = new Path(remoteUdfPath);
        boolean udfPathExsit = fileSystem.exists(hdfsPath);
        if (!udfPathExsit) {
            return;
        }
        // 创建本地路径
        java.nio.file.Path localPluginPath = Paths.get(localPluginPathStr);
        if (!localPluginPath.toFile().exists()) {
            Files.createDirectory(localPluginPath);
        }
        // 是否可写
        if (!Files.isWritable(localPluginPath)) {
            logger.info("maybe write to local path error!");
            return;
        }
        FileStatus[] fileStatuses = fileSystem.listStatus(hdfsPath);
        for (FileStatus fileStatus : fileStatuses) {
            String metaFileName = fileStatus.getPath().getName();
            if (fileStatus.isFile() && metaFileName.endsWith(META_FILE_FORMAT)) {
                // 元数据文件信息
                // 读取元数据信息
                byte[] metaContent = new byte[Long.valueOf(fileStatus.getLen()).intValue()];
                IOUtils.readFully(fileSystem.open(fileStatus.getPath()), metaContent);
                String metaStr = new String(metaContent, StandardCharsets.UTF_8);

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
                FunctionDefBean functionDefBean = new FunctionDefBean();
                functionDefBean.setFunctionName(metaInfoList.get(0));
                functionDefBean.setFunctionClass(metaInfoList.get(1));
                functionDefBean.setFunctionLocation(metaInfoList.get(2));
                functionDefBean.setFunctionFileMd5(metaInfoList.get(3));

                try {
                    // local file
                    String jarFileName = metaFileName.substring(0, metaFileName.lastIndexOf(".")) + JAR_FILE_FORMAT;
                    File localJarFile = Paths.get(localPluginPathStr + File.separator + jarFileName).toFile();
                    File localMetaFile = Paths.get(localPluginPathStr + File.separator + metaFileName).toFile();
                    // 本地是否存在
                    boolean overrideFile = true;
                    if (localJarFile.exists()) {
                        String localMd5 = MD5FileUtils.computeMd5ForFile(localJarFile).toString();
                        // 判断本地是否最新
                        overrideFile = !functionDefBean.getFunctionFileMd5().equals(localMd5);
                    }
                    if (overrideFile) {
                        logger.info("to override local file");
                        Path jarRemotePath = new Path(hdfsPath.toString(), jarFileName);
                        if (fileSystem.exists(jarRemotePath)) {
                            FileUtils.copyInputStreamToFile(fileSystem.open(jarRemotePath), localJarFile);
                            FileUtils.copyInputStreamToFile(fileSystem.open(fileStatus.getPath()), localMetaFile);
                        } else {
                            logger.error("remote jar file:{} not fund", jarRemotePath);
                        }
                    }
                } catch (Exception exception) {
                    logger.error("function info:{},update local udf jar files error:{}", JsonUtil.toJson(functionDefBean), exception.getMessage(), exception);
                }
            }
        }
    }

    /**
     * loadExternalFileUserDir
     *
     * @param shipFileDir
     * @return java.util.List<java.net.URL>
     * @author tasher
     * @created 2022/2/22
     */
    public static List<URL> loadExternalURLUserDir(String shipFileDir) {
        if (StringUtils.isBlank(shipFileDir)) {
            return Lists.newArrayList();
        }
        File syncFile = new File(shipFileDir);
        return Arrays.stream(Objects.requireNonNull(syncFile.listFiles()))
                .map(
                        file -> {
                            try {
                                return file.toURI().toURL();
                            } catch (MalformedURLException e) {
                                logger.error(
                                        "load loadExternalURLUserDir error:{}", e.getMessage(), e);
                                return null;
                            }
                        })
                .filter(url -> null != url && !url.getPath().endsWith("zip"))
                .collect(Collectors.toList());
    }

    /**
     * loadExternalFileUserDir
     *
     * @param shipFileDir
     * @return java.util.List<java.net.URL>
     * @author tasher
     * @created 2022/2/22
     */
    public static List<File> loadExternalFileUserDir(String shipFileDir) {
        if (StringUtils.isBlank(shipFileDir)) {
            return Lists.newArrayList();
        }
        File syncFile = new File(shipFileDir);
        return Arrays.stream(Objects.requireNonNull(syncFile.listFiles()))
                .filter(file -> !file.getName().endsWith("zip"))
                .collect(Collectors.toList());
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
    public static List<File> getShipJarFiles(String shipJars) throws MalformedURLException {
        if (StringUtils.isBlank(shipJars)) {
            return Lists.newArrayList();
        }
        String[] jars = shipJars.split(";");
        if (jars.length <= 0) {
            return Lists.newArrayList();
        }

        List<File> jarNames = Lists.newArrayList();
        for (String jar : jars) {
            if (jar.length() <= 1 || !jar.endsWith(".jar")) {
                continue;
            }
            // 这里是否要校验文件路径是否存在，待验证
            jarNames.add(new File(jar));
        }
        return jarNames;
    }

    /**
     * getUserJarFilesByLib
     *
     * @param libPath
     * @return java.util.Set<java.net.URL>
     * @author tasher
     * @created 2022/4/13
     */
    @SuppressWarnings("DuplicatedCode")
    public static List<File> getUserJarFilesByLib(String libPath) {
        if (StringUtils.isBlank(libPath)) {
            return Lists.newArrayList();
        }
        File tempFile = new File(libPath);
        if (tempFile.exists() && tempFile.isDirectory()) {
            List<File> jarNames = Lists.newArrayList();
            File[] jarFiles =
                    tempFile.listFiles((dir, file) -> file.toLowerCase().endsWith(".jar"));
            if (Objects.nonNull(jarFiles) && jarFiles.length > 0) {
                jarNames.addAll(Arrays.asList(jarFiles));
            }
            return jarNames;
        }
        return Lists.newArrayList();
    }
}
