package io.moyada.sharingan.infrastructure.invoke.data;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class HttpInvocation extends InvocationMetaDate {

    /**
     * http请求类型
     */
    private String httpType;

    private boolean hasBody;

    /**
     * 头信息
     */
    private String[] header;

    /**
     * 参数名
     */
    private String[] param;

    private String contentType;

    public HttpInvocation(String applicationName, String serviceName, String methodName, String httpType, String contentType) {
        super(applicationName, serviceName, methodName);
        this.httpType = httpType;
        this.contentType = contentType;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    public void setHasBody(boolean hasBody) {
        this.hasBody = hasBody;
    }

    public boolean isHasBody() {
        return hasBody;
    }

    public String getHttpType() {
        return httpType;
    }

    public String getContentType() {
        return contentType;
    }

    public String[] getHeader() {
        return header;
    }

    public String[] getParam() {
        return param;
    }
}
