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

package com.jm.flink.base.classloader;


import com.jm.flink.base.utils.ExceptionUtil;
import com.jm.flink.base.utils.ReflectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类加载管理器
 *
 * @author tasher
 * @created 2022/4/11
 * @return
 */
public class ClassLoaderManager {

    private static final Logger logger = LoggerFactory.getLogger(ClassLoaderManager.class);

    private static Map<String, URLClassLoader> pluginClassLoader = new ConcurrentHashMap<>();

    public static <R> R newInstance(Set<URL> jarUrls, ClassLoaderSupplier<R> supplier)
            throws Exception {
        ClassLoader classLoader = retrieveClassLoad(new ArrayList<>(jarUrls));
        return ClassLoaderSupplierCallBack.callbackAndReset(supplier, classLoader);
    }

    private static URLClassLoader retrieveClassLoad(List<URL> jarUrls) {
        jarUrls.sort(Comparator.comparing(URL::toString));
        String jarUrlkey = StringUtils.join(jarUrls, "_");
        return pluginClassLoader.computeIfAbsent(
                jarUrlkey,
                k -> {
                    try {
                        URL[] urls = jarUrls.toArray(new URL[0]);
                        ClassLoader parentClassLoader =
                                Thread.currentThread().getContextClassLoader();
                        URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);
                        logger.info("jarUrl:{} create ClassLoad successful...", jarUrlkey);
                        return classLoader;
                    } catch (Throwable e) {
                        logger.error(
                                "retrieve ClassLoad happens error:{}",
                                ExceptionUtil.getErrorMessage(e));
                        throw new RuntimeException("retrieve ClassLoad happens error");
                    }
                });
    }

    public static Set<URL> getClassPath() {
        Set<URL> classPaths = new HashSet<>();
        for (Map.Entry<String, URLClassLoader> entry : pluginClassLoader.entrySet()) {
            classPaths.addAll(Arrays.asList(entry.getValue().getURLs()));
        }
        return classPaths;
    }

    public static URLClassLoader loadExtraJar(List<URL> jarUrlList, URLClassLoader classLoader)
            throws IllegalAccessException, InvocationTargetException {
        for (URL url : jarUrlList) {
            if (url.toString().endsWith(".jar")) {
                urlClassLoaderAddUrl(classLoader, url);
            }
        }
        return classLoader;
    }

    private static void urlClassLoaderAddUrl(URLClassLoader classLoader, URL url)
            throws InvocationTargetException, IllegalAccessException {
        Method method = ReflectionUtils.getDeclaredMethod(classLoader, "addURL", URL.class);

        if (method == null) {
            throw new RuntimeException(
                    "can't not find declared method addURL, curr classLoader is "
                            + classLoader.getClass());
        }
        method.setAccessible(true);
        method.invoke(classLoader, url);
    }
}
