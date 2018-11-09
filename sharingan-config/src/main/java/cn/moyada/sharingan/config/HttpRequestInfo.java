package cn.moyada.sharingan.config;

/**
 * HTTP调用信息
 * @author xueyikang
 * @since 0.0。1
 **/
public class HttpRequestInfo {

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

    public String getHttpType() {
        return httpType;
    }

    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }
}
