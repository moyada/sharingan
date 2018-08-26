package cn.moyada.dubbo.faker.core.http;

import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

/**
 * @author xueyikang
 * @create 2018-02-03 20:36
 */
public class FiberHttpClient extends AbstractHttpClient {

    private final CloseableHttpClient client;

    public FiberHttpClient(int total, boolean ssl, List<HttpCookie> httpCookies) {
        int ioThreads = total / 3;

        client = FiberHttpClientBuilder.
                create(ioThreads <= 0 ? 1 : ioThreads)
                .setMaxConnPerRoute(3)
                .setMaxConnTotal(total)
                .setDefaultRequestConfig(buildRequestConfig(DEFAULT_TIMEOUT))
                .setDefaultCookieStore(buildCookies(httpCookies))
                .setSSLContext(buildSSL(ssl))
                .build();
    }
}
