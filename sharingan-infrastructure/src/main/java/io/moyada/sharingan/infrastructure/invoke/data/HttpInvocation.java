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

    /**
     * 头信息
     */
    private String[] header;

    /**
     * 参数名
     */
    private String[] param;

    public HttpInvocation(String applicationName, String serviceName, String methodName,
                          String httpType, String[] header, String[] param) {
        super(applicationName, serviceName, methodName);
        this.httpType = httpType;
        this.header = header;
        this.param = param;
    }

    public String getHttpType() {
        return httpType;
    }

    public String[] getHeader() {
        return header;
    }

    public String[] getParam() {
        return param;
    }
}
