package cn.moyada.faker.core.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author xueyikang
 * @create 2018-01-03 01:06
 */
public class HttpSyncClient {
    private Logger logger = LoggerFactory.getLogger(HttpSyncClient.class);

    private static int socketTimeout = 1000;// 设置等待数据超时时间5秒钟 根据业务调整

    private static int connectTimeout = 2000;// 连接超时

    private static int maxConnNum = 4000;// 连接池最大连接数

    private static int maxPerRoute = 1500;// 每个主机的并发最多只有1500

    private static PoolingClientConnectionManager cm;

    private static HttpParams httpParams;

    private static final String DEFAULT_ENCODING = Charset.defaultCharset()
            .name();

    // proxy代理相关配置
    private String host = "";
    private int port = 0;
    private String username = "";
    private String password = "";

    private DefaultHttpClient httpClient;

    private DefaultHttpClient proxyHttpClient;

    // 应用启动的时候就应该执行的方法
    public HttpSyncClient() {

        this.httpClient = createClient(false);

        this.proxyHttpClient = createClient(true);
    }

    public DefaultHttpClient createClient(boolean proxy) {

        SchemeRegistry sr = new SchemeRegistry();
        sr.register(new Scheme("http", 80, PlainSocketFactory
                .getSocketFactory()));
        SSLSocketFactory sslFactory;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[] { tm },
                    new java.security.SecureRandom());
            sslFactory = new SSLSocketFactory(sslContext,
                    SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            sr.register(new Scheme("https", 443, sslFactory));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 初始化连接池
        cm = new PoolingClientConnectionManager(sr);
        cm.setMaxTotal(maxConnNum);
        cm.setDefaultMaxPerRoute(maxPerRoute);

        httpParams = new BasicHttpParams();
        httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                connectTimeout);// 请求超时时间
        httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
                socketTimeout);// 读取数据超时时间
        // 如果启用了NoDelay策略，httpclient和站点之间传输数据时将会尽可能及时地将发送缓冲区中的数据发送出去、而不考虑网络带宽的利用率，这个策略适合对实时性要求高的场景
        httpParams.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);
        httpParams.setBooleanParameter(
                CoreConnectionPNames.STALE_CONNECTION_CHECK, true);

        DefaultHttpClient httpclient = new DefaultHttpClient(cm, httpParams);

        if (proxy) {
            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope(host, port),
                    new UsernamePasswordCredentials(username, password));
        }

        return httpclient;

    }

    public DefaultHttpClient getHttpClient() {
        return httpClient;
    }

    public DefaultHttpClient getProxyClient() {
        return proxyHttpClient;
    }

    public String httpGet(String url, List<BasicNameValuePair> parameters) {

        DefaultHttpClient client = getHttpClient();// 默认会到池中查询可用的连接，如果没有就新建
        HttpGet getMethod = null;
        String returnValue = "";
        try {
            getMethod = new HttpGet(url);

            if (null != parameters) {
                String params = EntityUtils.toString(new UrlEncodedFormEntity(
                        parameters, DEFAULT_ENCODING));
                getMethod.setURI(new URI(getMethod.getURI().toString() + "?"
                        + params));
                logger.debug("httpGet-getUrl:{}", getMethod.getURI());
            }

            HttpResponse response = client.execute(getMethod);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                HttpEntity he = response.getEntity();
                returnValue = new String(EntityUtils.toByteArray(he),
                        DEFAULT_ENCODING);
                return returnValue;
            }

        } catch (UnsupportedEncodingException e) {
            logger.error(Thread.currentThread().getName()
                    + "httpGet Send Error,Code error:" + e.getMessage());
        } catch (ClientProtocolException e) {
            logger.error(Thread.currentThread().getName()
                    + "httpGet Send Error,Protocol error:" + e.getMessage());
        } catch (IOException e) {
            logger.error(Thread.currentThread().getName()
                    + "httpGet Send Error,IO error:" + e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(Thread.currentThread().getName()
                    + "httpGet Send Error,IO error:" + e.getMessage());
        } finally {// 释放连接,将连接放回到连接池
            getMethod.releaseConnection();

        }
        return returnValue;

    }

    public String httpPost(String url, List<BasicNameValuePair> parameters,
                           String requestBody) {

        DefaultHttpClient client = getHttpClient();// 默认会到池中查询可用的连接，如果没有就新建
        HttpPost postMethod = null;
        String returnValue = "";
        try {
            postMethod = new HttpPost(url);

            if (null != parameters) {
                String params = EntityUtils.toString(new UrlEncodedFormEntity(
                        parameters, DEFAULT_ENCODING));
                postMethod.setURI(new URI(postMethod.getURI().toString() + "?"
                        + params));
                logger.debug("httpPost-getUrl:{}", postMethod.getURI());
            }

            if (StringUtils.isNotBlank(requestBody)) {
                StringEntity se = new StringEntity(requestBody,
                        DEFAULT_ENCODING);
                postMethod.setEntity(se);
            }

            HttpResponse response = client.execute(postMethod);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                HttpEntity he = response.getEntity();
                returnValue = new String(EntityUtils.toByteArray(he),
                        DEFAULT_ENCODING);
                return returnValue;
            }

        } catch (UnsupportedEncodingException e) {
            logger.error(Thread.currentThread().getName()
                    + "httpPost Send Error,Code error:" + e.getMessage());
        } catch (ClientProtocolException e) {
            logger.error(Thread.currentThread().getName()
                    + "httpPost Send Error,Protocol error:" + e.getMessage());
        } catch (IOException e) {
            logger.error(Thread.currentThread().getName()
                    + "httpPost Send Error,IO error:" + e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(Thread.currentThread().getName()
                    + "httpPost Send Error,IO error:" + e.getMessage());
        } finally {// 释放连接,将连接放回到连接池
            postMethod.releaseConnection();
            // 释放池子中的空闲连接
            // client.getConnectionManager().closeIdleConnections(30L,
            // TimeUnit.MILLISECONDS);
        }
        return returnValue;

    }
}
