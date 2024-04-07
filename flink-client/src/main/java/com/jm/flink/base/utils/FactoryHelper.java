/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jm.flink.base.utils;


import com.jm.flink.base.constants.ConstantValue;
import com.jm.flink.base.enums.PluginMode;
import com.jm.flink.base.exception.FlinkDoRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.factories.TableFactoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.jm.flink.constant.FlinkDoConstants.CLASS_FILE_NAME_FMT;


/**
 * 专注于加载插件环境
 *
 * @author jinmu
 * @created 2022/4/24
 */
public class FactoryHelper {


    private static final Logger LOG = LoggerFactory.getLogger(TableFactoryService.class);


    /**
     * 插件路径
     */
    protected String pluginPath = null;

    /**
     * 插件加载类型(默认)
     */
    protected String pluginLoadMode = PluginMode.SHIPFILE.getName();

    /**
     * 上下文环境
     */
    protected StreamExecutionEnvironment env = null;

    /**
     * shipfile需要的jar
     */
    protected List<URL> classPathSet = new ArrayList<>();

    /**
     * shipfile需要的jar的classPath index
     */
    protected int classFileNameIndex = 0;

    /**
     * 任务执行模式
     */
    protected String executionMode;

    /**
     * default struct
     */
    public FactoryHelper() {
    }

    /**
     * 注册插件到StreamExecutionEnvironment,并且包装Class_Path给外围作业上传
     *
     * <p>See { useful FactoryUtil,TableFactoryService}
     *
     * @param ignore
     * @return void
     * @author jinmu
     * @param: factoryIdentifier
     * @param: classLoader
     * @created 2022/4/24
     */
    public void registerCachedFile(
            String factoryIdentifier, ClassLoader classLoader, boolean ignore) {

        if (StringUtils.isBlank(this.pluginPath)) {
            LOG.warn(
                    "FactoryHelper registerCachedFile stop, warning message: this pluginPath not set!!!");
            return;
        }

        String pluginJarPath =
                pluginPath
                        + File.separatorChar
                        + ConstantValue.CONNECTOR_DIR_NAME
                        + File.separatorChar
                        + factoryIdentifier;
        try {
            File pluginJarPathFile = new File(pluginJarPath);
            // 路径不存在或者不为文件夹
            if (!pluginJarPathFile.exists() || !pluginJarPathFile.isDirectory()) {
                if (ignore) {
                    return;
                } else {
                    throw new FlinkDoRuntimeException(
                            "plugin path:" + pluginJarPath + " is not exist.");
                }
            }

            File[] files =
                    pluginJarPathFile.listFiles(
                            tmpFile -> tmpFile.isFile() && tmpFile.getName().endsWith(".jar"));
            if (files == null || files.length == 0) {
                throw new FlinkDoRuntimeException("plugin path:" + pluginJarPath + " is null.");
            }
            List<String> urlList = new ArrayList<String>();
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);

            for (File file : files) {
                URL jarUrl = file.toURI().toURL();
                add.invoke(classLoader, jarUrl);
                if (!this.classPathSet.contains(jarUrl)) {
                    urlList.add(jarUrl.toString());
                    this.classPathSet.add(jarUrl);
                    String classFileName =
                            String.format(CLASS_FILE_NAME_FMT, this.classFileNameIndex);
                    this.env.registerCachedFile(jarUrl.getPath(), classFileName, true);
                    this.classFileNameIndex++;
                }
            }
            PluginUtil.setPipelineOptionsToEnvConfig(this.env, urlList, executionMode);
        } catch (Exception e) {
            LOG.warn("can't add jar in {} to cachedFile, error:{}", pluginJarPath, e.getMessage());
        }
    }

    /**
     * register plugin jar file
     *
     * @param factoryIdentifier
     * @param classLoader
     * @param dirName
     */
    public void registerCachedFile(
            String factoryIdentifier, ClassLoader classLoader, String dirName) {
        Set<URL> urlSet =
                PluginUtil.getJarFileDirPath(
                        factoryIdentifier, this.pluginPath, dirName);
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);
            List<String> urlList = new ArrayList<>(urlSet.size());
            for (URL jarUrl : urlSet) {
                add.invoke(classLoader, jarUrl);
                if (!this.classPathSet.contains(jarUrl)) {
                    urlList.add(jarUrl.toString());
                    this.classPathSet.add(jarUrl);
                    String classFileName =
                            String.format(
                                    CLASS_FILE_NAME_FMT, this.classFileNameIndex);
                    this.env.registerCachedFile(jarUrl.getPath(), classFileName, true);
                    this.classFileNameIndex++;
                }
            }
            PluginUtil.setPipelineOptionsToEnvConfig(this.env, urlList, executionMode);
        } catch (Exception e) {
            LOG.warn("can't add jar in {} to cachedFile, e = {}", urlSet, e.getMessage());
        }
    }

    public void setPluginLoadMode(String pluginLoadMode) {
        this.pluginLoadMode = pluginLoadMode;
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
    }

    public void setEnv(StreamExecutionEnvironment env) {
        this.env = env;
    }

    public void setExecutionMode(String executionMode) {
        this.executionMode = executionMode;
    }
}
