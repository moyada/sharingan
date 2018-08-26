package cn.moyada.faker.module.fetch;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;

/**
 * 获取nexus下最近jar包
 * @author xueyikang
 * @create 2018-04-27 15:00
 */
public abstract class AbstractFetchLastJar {

    private final HttpClient instance;

    public AbstractFetchLastJar() {
        this.instance = HttpClientBuilder.create().build();
    }

    /**
     * 发送请求
     * @param request
     * @return
     */
    protected String request(HttpUriRequest request) {
        String req;
        try {
            HttpResponse execute = instance.execute(request);
            req = EntityUtils.toString(execute.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            req = null;
        }
        return req;
    }

    protected HttpPost getHttpPost(String request) {
        HttpPost httpPost = new HttpPost(request);
        httpPost.setHeader("ContentType", MimeTypeUtils.APPLICATION_JSON_VALUE);
        return httpPost;
    }
}
