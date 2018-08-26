package cn.moyada.dubbo.faker.core.http;

import cn.moyada.dubbo.faker.core.model.HttpInvokeInfo;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author xueyikang
 * @create 2018-02-03 20:41
 */
public abstract class AbstractHttpClient {

    protected static final int DEFAULT_TIMEOUT = 3000;

    protected HttpPost buildPost(HttpInvokeInfo httpInvokeInfo) {
        HttpPost httpPost = new HttpPost();
        httpPost.setURI(URI.create(httpInvokeInfo.getUrl()));
        httpPost.setEntity(httpInvokeInfo.getHttpEntity());
        httpPost.setHeader(httpInvokeInfo.getHeader());
        return httpPost;
    }

    protected static RequestConfig buildRequestConfig(int timeout) {
        return RequestConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();
    }

    protected static CookieStore buildCookies(List<HttpCookie> httpCookies) {
        if(null == httpCookies || httpCookies.isEmpty()) {
            return null;
        }
        BasicCookieStore cookieStore = new BasicCookieStore();

        httpCookies.forEach(httpCookie -> {
            BasicClientCookie cookie = new BasicClientCookie(httpCookie.getName(), httpCookie.getValue());
            cookie.setDomain(httpCookie.getDomain());
            cookie.setPath(httpCookie.getPath());
            cookieStore.addCookie(cookie);
        });
        return cookieStore;
    }

    protected static SSLContext buildSSL(boolean ssl) {
        if(!ssl) {
            return null;
        }
        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
            sslContext = null;
        }
        return sslContext;
    }
}
