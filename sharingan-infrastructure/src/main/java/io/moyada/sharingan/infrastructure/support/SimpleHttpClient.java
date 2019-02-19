package io.moyada.sharingan.infrastructure.support;


import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * http客户端
 * @author xueyikang
 * @create 2018-04-27 15:00
 */
public class SimpleHttpClient {

    private final HttpClient instance;

    public SimpleHttpClient() {
        this.instance = HttpClientBuilder.create().build();
    }

    public SimpleHttpClient(String userName, String password) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials(userName, password);
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        this.instance = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
    }

    /**
     * 发送请求
     * @param request
     * @return
     */
    private String request(HttpUriRequest request) {
        String req;
        try {
            HttpResponse execute = instance.execute(request);
            req = EntityUtils.toString(execute.getEntity());
        } catch (IOException e) {
            req = null;
        }
        return req;
    }

    /**
     * POST请求
     * @param url
     * @param body
     * @return
     */
    public String post(String url, String body) {
        HttpPost httpPost = new HttpPost(url);
        setHeader(httpPost);
        httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        return request(httpPost);
    }

    /**
     * GET请求
     * @param url
     * @return
     */
    public String get(String url) {
        HttpGet httpGet = new HttpGet(url);
        setHeader(httpGet);
        return request(httpGet);
    }

    /**
     * 设置头信息
     * @param request
     */
    private void setHeader(HttpUriRequest request) {
        request.setHeader("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE);
    }
}
