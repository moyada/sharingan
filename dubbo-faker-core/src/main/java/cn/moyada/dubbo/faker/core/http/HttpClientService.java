package cn.moyada.dubbo.faker.core.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author xueyikang
 * @create 2018-01-03 01:12
 */
public class HttpClientService {
    private static Logger LOG = LoggerFactory.getLogger(HttpClientService.class);

    protected void exeAsyncReq(String baseUrl, boolean isPost,
                               List<BasicNameValuePair> urlParams,
                               List<BasicNameValuePair> postBody, FutureCallback callback)
            throws Exception {

        if (baseUrl == null) {
            LOG.warn("we don't have base url, check config");
            throw new Exception("missing base url");
        }

        HttpRequestBase httpMethod;
        CloseableHttpAsyncClient hc;

        try {
            hc = HttpClientFactory.getInstance().getHttpAsyncClientPool()
                    .getAsyncHttpClient();

            hc.start();

            HttpClientContext localContext = HttpClientContext.create();
            BasicCookieStore cookieStore = new BasicCookieStore();

            if (isPost) {
                httpMethod = new HttpPost(baseUrl);

                if (null != postBody) {
                    LOG.debug("exeAsyncReq post postBody={}", postBody);
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                            postBody, StandardCharsets.UTF_8);
                    ((HttpPost) httpMethod).setEntity(entity);
                }

                if (null != urlParams) {

                    String getUrl = EntityUtils
                            .toString(new UrlEncodedFormEntity(urlParams));

                    httpMethod.setURI(new URI(httpMethod.getURI().toString()
                            + "?" + getUrl));
                }

            } else {

                httpMethod = new HttpGet(baseUrl);

                if (null != urlParams) {

                    String getUrl = EntityUtils
                            .toString(new UrlEncodedFormEntity(urlParams));

                    httpMethod.setURI(new URI(httpMethod.getURI().toString()
                            + "?" + getUrl));
                }
            }

            System.out.println("exeAsyncReq getparams:" + httpMethod.getURI());

            localContext.setAttribute(HttpClientContext.COOKIE_STORE,
                    cookieStore);

            hc.execute(httpMethod, localContext, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected String getHttpContent(HttpResponse response) {
        HttpEntity entity = response.getEntity();

        if (entity == null) {
            return null;
        }

        String body;
        try {
            body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (ParseException | IOException e) {
            LOG.warn("the response's content inputstream is corrupt", e);
            body = null;
        }
        return body;
    }
}
