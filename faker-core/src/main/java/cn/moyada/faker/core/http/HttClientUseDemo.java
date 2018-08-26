package cn.moyada.faker.core.http;

/**
 * @author xueyikang
 * @create 2018-01-03 01:13
 */

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * http client 使用
 * */
public class HttClientUseDemo extends HttpClientService {

    public static void main(String[] args) {

        new HttClientUseDemo().getConfCall();
    }

    public void getConfCall() {

        String url = "http://220.181.14.110/xxxxx/xxxxx/searchbyappid.do";

        List<BasicNameValuePair> urlParams = new ArrayList<>();
        urlParams.add(new BasicNameValuePair("appid", "2"));
        exeHttpReq(url, false, urlParams, null, new HttpCallback());
    }

    public void exeHttpReq(String baseUrl, boolean isPost,
                           List<BasicNameValuePair> urlParams,
                           List<BasicNameValuePair> postBody,
                           FutureCallback<HttpResponse> callback) {

        try {
            System.out.println("enter exeAsyncReq");
            exeAsyncReq(baseUrl, isPost, urlParams, postBody, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}