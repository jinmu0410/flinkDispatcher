package com.jm.flink.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils {

    private final static Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * urlToFileInputStream
     * 备注: 自行处理stream close
     *
     * @param urlPath
     * @return java.io.InputStream
     * @author jinmu
     * @created 2021/9/10
     */
    public static InputStream urlToFileInputStream(String urlPath) {
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000 * 5);
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            // 文件大小
            int fileLength = httpURLConnection.getContentLength();
            // 控制台打印文件大小
            log.info("您要下载的文件大小为:" + fileLength + "byte");
            // 建立链接从请求中获取数据
            return httpURLConnection.getInputStream();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            log.error("down net file error:{}", e.getMessage(), e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("down net file error: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * getUrlFileName
     *
     * @param url
     * @return java.lang.String
     * @author jinmu
     * @created 2021/9/10
     */
    public static String getUrlFileName(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        // 去除,
        return url.substring(url.lastIndexOf(File.separator) + 1).replaceAll(",", "");
    }
}
