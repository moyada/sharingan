package cn.moyada.dubbo.faker.core.http;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author xueyikang
 * @create 2018-01-03 01:06
 */
public class HttpSyncClient2 extends AbstractHttpClient {
    private Logger logger = LoggerFactory.getLogger(HttpSyncClient2.class);

    private HttpClient client;

    public void excuteGet(HttpInvoke httpInvoke) {

    }

    public void excutePost(HttpInvoke httpInvoke) {

    }


    protected URI buildURI(HttpParameter httpParameter) {
        URI uri;
        try {
            uri = new URIBuilder()
                    .setScheme(httpParameter.getHttpScheme().getScheme())
                    .setHost(httpParameter.getHost())
                    .setPort(httpParameter.getPort())
                    .setPath(httpParameter.getPath())
                    .setParameters(httpParameter.getParams())
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            uri = null;
        }
        return uri;
    }

    public static HttpSyncClient2 buildHttpClient(boolean ssl, List<HttpCookie> httpCookies) {
        HttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(buildRequestConfig(DEFAULT_TIMEOUT))
                .setDefaultCookieStore(buildCookies(httpCookies))
                .setSSLContext(buildSSL(ssl))
                .build();
        HttpSyncClient2 httpSyncClient2 = new HttpSyncClient2();
        httpSyncClient2.client = client;
        return httpSyncClient2;
    }
}
