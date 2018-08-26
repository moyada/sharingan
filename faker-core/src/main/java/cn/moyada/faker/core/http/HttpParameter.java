package cn.moyada.faker.core.http;

import cn.moyada.dubbo.faker.core.enums.HttpScheme;
import org.apache.http.NameValuePair;

import java.util.List;

/**
 * @author xueyikang
 * @create 2018-01-07 12:34
 */
public class HttpParameter {

    private HttpScheme httpScheme;

    private String host;

    private int port;

    private String path;

    private List<NameValuePair> params;

    public HttpScheme getHttpScheme() {
        return httpScheme;
    }

    public void setHttpScheme(HttpScheme httpScheme) {
        this.httpScheme = httpScheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<NameValuePair> getParams() {
        return params;
    }

    public void setParams(List<NameValuePair> params) {
        this.params = params;
    }
}
