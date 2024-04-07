package com.jm.flink.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

/**
 * HTTP工具类 版本: 4.5.12 说明:
 * <p>
 * 1. 所有请求返回HttpEntity,自行通过 Apache EntityUtils 进行结果转换; 2. HTTPS URL 将会自动建立SSL Client
 * </p>
 *
 * @author jinmu
 * @created 2021/4/19
 * @return
 */
public class HttpClientUtils {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HttpClientUtils.class);

    public static final String DEFAULT_CONNTENT_TYPE = "application/json";
    public static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 10000;
    private static final String DEFAULT_CHARSET = "UTF-8";

    private static SSLContext ctx = null;

    private static SSLConnectionSocketFactory socketFactory = null;

    private static int keepAliveTimeout = 0;

    /**
     * 是否校验SSL服务端证书，默认为需要校验
     */
    private static volatile boolean needCheckServerTrusted = true;

    /**
     * doGet
     * <p>
     * 超时时间按照默认配置
     * </P>
     *
     * @return org.apache.http.HttpEntity
     * @author jinmu
     * @param: url
     * @param: headers
     * @param: querys
     * @created 2021/5/19
     */
    public static HttpEntity doGet(String url)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpGet request = new HttpGet(buildUrl(url, null));
        wrapHeader(request, null);
        wrapConfig(request, null);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doGet
     *
     * @return org.apache.http.HttpEntity
     * @author jinmu
     * @param: url
     * @created 2021/5/19
     */
    public static HttpEntity doGet(String url, RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpGet request = new HttpGet(buildUrl(url, null));
        wrapHeader(request, null);
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doGet
     *
     * @return org.apache.http.HttpEntity
     * @author jinmu
     * @param: url
     * @param: headers
     * @param: querys
     * @created 2021/5/19
     */
    public static HttpEntity doGet(String url, Map<String, String> headers,
                                   Map<String, String> querys, RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpGet request = new HttpGet(buildUrl(url, querys));
        wrapHeader(request, headers);
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doPostForm
     * <p>
     * 超时时间按照默认配置
     * </P>
     *
     * @return org.apache.http.HttpEntity
     * @author jinmu
     * @param: url
     * @created 2021/5/19
     */
    public static HttpEntity doPostForm(String url, Map<String, String> bodys)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpPost request = new HttpPost(buildUrl(url, null));
        wrapHeader(request, null);
        warpFormBody(bodys, request);
        wrapConfig(request, null);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doPostForm
     *
     * @return org.apache.http.HttpResponse
     * @author jinmu
     * @param: url
     * @param: headers
     * @param: querys
     * @created 2021/5/14
     */
    public static HttpEntity doPostForm(String url, Map<String, String> bodys,
                                        RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpPost request = new HttpPost(buildUrl(url, null));
        wrapHeader(request, null);
        warpFormBody(bodys, request);
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doPostForm
     *
     * @return org.apache.http.HttpResponse
     * @author jinmu
     * @param: url
     * @param: headers
     * @param: querys
     * @created 2021/5/14
     */
    public static HttpEntity doPostForm(String url,
                                        Map<String, String> headers,
                                        Map<String, String> querys,
                                        Map<String, String> bodys, RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpPost request = new HttpPost(buildUrl(url, querys));
        wrapHeader(request, headers);
        warpFormBody(bodys, request);
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doPostStr
     * <p>
     * 超时时间按照默认配置
     * </P>
     *
     * @return org.apache.http.HttpEntity
     * @author jinmu
     * @param: url
     * @created 2021/5/19
     */
    public static HttpEntity doPostStr(String url,
                                       String body)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpPost request = new HttpPost(buildUrl(url, null));
        wrapHeader(request, null);
        if (StringUtils.isNotBlank(body)) {
            request.setEntity(buildStringEntity(body, null));
        }
        wrapConfig(request, null);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doPostStr
     *
     * @return org.apache.http.HttpEntity
     * @author jinmu
     * @param: url
     * @param: body
     * @created 2021/5/19
     */
    public static HttpEntity doPostStr(String url,
                                       String body, RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpPost request = new HttpPost(buildUrl(url, null));
        wrapHeader(request, null);
        if (StringUtils.isNotBlank(body)) {
            request.setEntity(buildStringEntity(body, null));
        }
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doPostStr
     * <p>
     * 超时时间按照默认配置
     * </P>
     *
     * @return org.apache.http.HttpResponse
     * @author jinmu
     * @param: url
     * @param: headers
     * @param: querys
     * @param: body
     * @created 2021/5/14
     */
    public static HttpEntity doPostStr(String url,
                                       Map<String, String> headers,
                                       Map<String, String> querys,
                                       String body, String contentType, RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpPost request = new HttpPost(buildUrl(url, querys));
        wrapHeader(request, headers);
        if (StringUtils.isNotBlank(body)) {
            request.setEntity(buildStringEntity(body, contentType));
        }
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doPostStream
     *
     * @return org.apache.http.HttpResponse
     * @author jinmu
     * @param: url
     * @param: method
     * @param: headers
     * @param: querys
     * @created 2021/5/14
     */
    public static HttpEntity doPostStream(String url, String method,
                                          Map<String, String> headers,
                                          Map<String, String> querys,
                                          byte[] body, RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);

        HttpPost request = new HttpPost(buildUrl(url, querys));
        wrapHeader(request, headers);
        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doPutStr
     *
     * @return org.apache.http.HttpEntity
     * @author jinmu
     * @param: url
     * @param: headers
     * @param: querys
     * @param: body
     * @param: contentType
     * @created 2021/5/19
     */
    public static HttpEntity doPutStr(String url,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      String body, String contentType, RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpPut request = new HttpPut(buildUrl(url, querys));
        wrapHeader(request, headers);
        if (StringUtils.isNotBlank(body)) {
            request.setEntity(buildStringEntity(body, contentType));
        }
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doPutStream
     *
     * @return org.apache.http.HttpResponse
     * @author jinmu
     * @param: url
     * @param: headers
     * @param: querys
     * @created 2021/5/14
     */
    public static HttpEntity doPutStream(String url,
                                         Map<String, String> headers,
                                         Map<String, String> querys,
                                         byte[] body, RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);

        HttpPut request = new HttpPut(buildUrl(url, querys));
        wrapHeader(request, headers);
        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * doDelete
     *
     * @return org.apache.http.HttpResponse
     * @author jinmu
     * @param: url
     * @param: headers
     * @created 2021/5/14
     */
    public static HttpEntity doDelete(String url,
                                      Map<String, String> headers,
                                      Map<String, String> querys, RequestConfig requestConfig)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpDelete request = new HttpDelete(buildUrl(url, querys));
        wrapHeader(request, headers);
        wrapConfig(request, requestConfig);
        return handlerHttpResponse(httpClient.execute(request));
    }

    /**
     * 默认证书管理器
     *
     * @author jinmu
     * @created 2021/5/14
     * @return
     */
    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    static {
        // 初始化SSL
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()},
                    new SecureRandom());
            ctx.getClientSessionContext().setSessionTimeout(15);
            ctx.getClientSessionContext().setSessionCacheSize(1000);
            socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            log.error("SSLContext init error,e:{}", e.getMessage(), e);
        }
    }

    /**
     * 设置是否校验SSL服务端证书
     *
     * @param needCheckServerTrusted true：需要校验（默认，推荐）；
     *                               <p>
     *                               false：不需要校验（仅当部署环境不便于进行服务端证书校验，且已有其他方式确保通信安全时，可以关闭SSL服务端证书校验功能）
     */
    public static void setNeedCheckServerTrusted(boolean needCheckServerTrusted) {
        HttpClientUtils.needCheckServerTrusted = needCheckServerTrusted;
    }

    /**
     * 设置KeepAlive连接超时时间，一次HTTP请求完成后，底层TCP连接将尝试尽量保持该超时时间后才关闭，以便其他HTTP请求复用TCP连接
     * <p>
     * KeepAlive连接超时时间设置为0，表示使用默认的KeepAlive连接缓存时长（目前为5s）
     * <p>
     * 连接并非一定能保持指定的KeepAlive超时时长，比如服务端断开了连接
     * <p>
     * 注：该方法目前只在JDK8上测试有效
     *
     * @param timeout KeepAlive超时时间，单位秒
     */
    public static void setKeepAliveTimeout(int timeout) {
        if (timeout < 0 || timeout > 60) {
            throw new RuntimeException("keep-alive timeout value must be between 0 and 60.");
        }
        keepAliveTimeout = timeout;
    }

    /**
     * buildRequestConfig
     *
     * @return org.apache.http.client.config.RequestConfig
     * @author jinmu
     * @param: connectTimeout
     * @created 2021/5/14
     */
    public static RequestConfig buildRequestConfig(int connectTimeout, int requestTimeOut) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout).setConnectionRequestTimeout(requestTimeOut)
                .setSocketTimeout(connectTimeout).build();
        return requestConfig;
    }

    /**
     * buildUrl
     *
     * @return java.lang.String
     * @author jinmu
     * @param: url
     * @created 2021/5/14
     */
    private static String buildUrl(String url, Map<String, String> querys)
            throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder(url);
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }
        return sbUrl.toString();
    }


    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder query = new StringBuilder();
        Set<Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }
                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }

        return query.toString();
    }

    /**
     * 构建请求配置
     *
     * @return void
     * @author jinmu
     * @param: httpRequestBase
     * @created 2021/5/19
     */
    private static void wrapConfig(HttpRequestBase httpRequestBase, RequestConfig requestConfig) {
        if (null == requestConfig) {
            requestConfig = buildRequestConfig(DEFAULT_CONNECT_TIMEOUT,
                    DEFAULT_CONNECTION_REQUEST_TIMEOUT);
        }
        httpRequestBase.setConfig(requestConfig);
    }

    /**
     * wrapHeader
     *
     * @return void
     * @author jinmu
     * @param: httpRequestBase
     * @created 2021/5/14
     */
    private static void wrapHeader(HttpRequestBase httpRequestBase, Map<String, String> headers) {
        if (null != headers && headers.entrySet().size() > 0) {
            for (Entry<String, String> e : headers.entrySet()) {
                httpRequestBase.addHeader(e.getKey(), e.getValue());
            }
        }
    }

    /**
     * handlerHttpResponse
     *
     * @return org.apache.http.HttpEntity
     * @author jinmu
     * @created 2021/5/14
     */
    private static HttpEntity handlerHttpResponse(HttpResponse httpResponse)
            throws IOException {
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            String responseStr = EntityUtils.toString(httpResponse.getEntity());
            log.error("HttpResponse error:{}", responseStr);
            throw new HttpResponseException(httpResponse.getStatusLine().getStatusCode(), responseStr);
        }
        return httpResponse.getEntity();
    }

    /**
     * buildStringEntity
     *
     * @return org.apache.http.entity.StringEntity
     * @author jinmu
     * @param: body
     * @created 2021/5/14
     */
    private static StringEntity buildStringEntity(String body, String contentType) {
        if (StringUtils.isNotBlank(body)) {
            StringEntity stringEntity = new StringEntity(body, "utf-8");
            stringEntity.setContentType(DEFAULT_CONNTENT_TYPE);
            if (StringUtils.isNotBlank(contentType)) {
                stringEntity.setContentType(contentType);
            }
            return stringEntity;
        }
        return null;
    }

    private static void warpFormBody(Map<String, String> bodys, HttpPost request)
            throws UnsupportedEncodingException {
        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }
    }

    /**
     * wrapClient
     *
     * @return org.apache.http.client.HttpClient
     * @author jinmu
     * @created 2021/5/14
     */
    private static HttpClient wrapClient(String url)
            throws KeyManagementException, NoSuchAlgorithmException {
        HttpClient httpClient = new DefaultHttpClient();
        if (url.startsWith("https://")) {
            sslClient(httpClient);
        }
        return httpClient;
    }

    /**
     * sslClient
     *
     * @return org.apache.http.client.HttpClient
     * @author jinmu
     * @created 2021/5/14
     */
    private static HttpClient sslClient(HttpClient httpClient) {
        // 创建Registry
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .setExpectContinueEnabled(Boolean.TRUE)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", socketFactory).build();
        // 创建ConnectionManager，添加Connection配置信息
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig).build();
        return closeableHttpClient;
    }

    /**
     * 使用默认的UTF-8字符集反编码请求参数值。
     *
     * @param value 参数值
     * @return 反编码后的参数值
     */
    public static String decode(String value) {
        return decode(value, DEFAULT_CHARSET);
    }

    /**
     * 使用默认的UTF-8字符集编码请求参数值。
     *
     * @param value 参数值
     * @return 编码后的参数值
     */
    public static String encode(String value) {
        return encode(value, DEFAULT_CHARSET);
    }

    /**
     * 使用指定的字符集反编码请求参数值。
     *
     * @param value   参数值
     * @param charset 字符集
     * @return 反编码后的参数值
     */
    public static String decode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 使用指定的字符集编码请求参数值。
     *
     * @param value   参数值
     * @param charset 字符集
     * @return 编码后的参数值
     */
    public static String encode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static Map<String, String> getParamsFromUrl(String url) {
        Map<String, String> map = null;
        if (url != null && url.indexOf('?') != -1) {
            map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
        }
        if (map == null) {
            map = new HashMap<String, String>();
        }
        return map;
    }

    /**
     * 从URL中提取所有的参数。
     *
     * @param query URL地址
     * @return 参数映射
     */
    public static Map<String, String> splitUrlQuery(String query) {
        Map<String, String> result = new HashMap<String, String>();

        String[] pairs = query.split("&");
        if (pairs != null && pairs.length > 0) {
            for (String pair : pairs) {
                String[] param = pair.split("=", 2);
                if (param != null && param.length == 2) {
                    result.put(param[0], param[1]);
                }
            }
        }

        return result;
    }

    /**
     * 由于HttpUrlConnection不支持设置KeepAlive超时时间，该方法通过反射机制设置
     *
     * @param connection 需要设置KeepAlive的连接
     */
    private static void setKeepAliveTimeout(HttpURLConnection connection) {
        if (keepAliveTimeout == 0) {
            return;
        }
        try {

            Field delegateHttpsUrlConnectionField = Class
                    .forName("sun.net.www.protocol.https.HttpsURLConnectionImpl").getDeclaredField(
                            "delegate");
            delegateHttpsUrlConnectionField.setAccessible(true);
            Object delegateHttpsUrlConnection = delegateHttpsUrlConnectionField.get(connection);

            Field httpClientField = Class.forName("sun.net.www.protocol.http.HttpURLConnection")
                    .getDeclaredField("http");
            httpClientField.setAccessible(true);
            Object httpClient = httpClientField.get(delegateHttpsUrlConnection);

            Field keepAliveTimeoutField = Class.forName("sun.net.www.http.HttpClient")
                    .getDeclaredField("keepAliveTimeout");
            keepAliveTimeoutField.setAccessible(true);
            keepAliveTimeoutField.setInt(httpClient, keepAliveTimeout);
        } catch (Throwable ignored) {
            //设置KeepAlive超时只是一种优化辅助手段，设置失败不应阻塞主链路，设置失败不应影响功能
        }
    }

}