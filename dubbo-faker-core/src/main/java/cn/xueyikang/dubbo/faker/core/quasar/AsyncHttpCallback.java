package cn.xueyikang.dubbo.faker.core.quasar;

import org.apache.http.concurrent.FutureCallback;

/**
 * @author xueyikang
 * @create 2018-01-02 14:52
 */
public class AsyncHttpCallback<HttpResponse> implements FutureCallback<HttpResponse> {

    @Override
    public void completed(HttpResponse httpResponse) {

    }

    @Override
    public void failed(Exception e) {

    }

    @Override
    public void cancelled() {

    }
}
