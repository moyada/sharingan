package cn.moyada.faker.core.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.concurrent.FutureCallback;

/**
 * 被回调的对象，给异步的httpclient使用
 * @author xueyikang
 * @create 2018-01-03 10:58
 */
public class HttpCallback extends HttpClientService implements FutureCallback<HttpResponse> {

    /**
     * 请求完成后调用该函数
     */
    @Override
    public void completed(HttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        String content = getHttpContent(response);

        HttpClientUtils.closeQuietly(response);
    }

    /**
     * 请求取消后调用该函数
     */
    @Override
    public void cancelled() {

    }

    /**
     * 请求失败后调用该函数
     */
    @Override
    public void failed(Exception e) {

    }
}
