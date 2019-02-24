package io.moyada.sharingan.domain.metadada;

import io.moyada.sharingan.infrastructure.invoke.data.HttpInvocation;

/**
 * http请求信息
 * @author xueyikang
 * @since 0.0.1
 **/
public class HttpData extends InvokeData<HttpInvocation> {

    /**
     * 方法编号
     */
    private Integer id;

    /**
     * 请求类别, GET\POST\PUT\DELETE
     */
    private String methodType;

    /**
     * 编码方式
     */
    private String contentType;

    /**
     * 表达式
     */
    private String param;

    /**
     * 表达式
     */
    private String header;

    /**
     * 表达式
     */
    private String body;

    private HttpData() {
    }

    @Override
    public HttpInvocation getInvocation() {
        ServiceData serviceData = getServiceData();
        AppData appData = serviceData.getAppData();
        return new HttpInvocation(appData.getName(), serviceData.getName(), getMethodName(), getMethodType(), contentType);
    }

    private void setId(Integer id) {
        this.id = id;
    }

    private void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    private void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public String getParam() {
        return param;
    }

    private void setParam(String param) {
        this.param = param;
    }

    public String getHeader() {
        return header;
    }

    private void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    private void setBody(String body) {
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public String getMethodType() {
        return methodType;
    }
}
