package cn.moyada.dubbo.faker.core.model;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

/**
 * @author xueyikang
 * @create 2018-02-03 21:04
 */
public class HttpInvokeInfo {

    private String url;

    private HttpEntity httpEntity;

    private Header header;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpEntity getHttpEntity() {
        return httpEntity;
    }

    public void setHttpEntity(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
