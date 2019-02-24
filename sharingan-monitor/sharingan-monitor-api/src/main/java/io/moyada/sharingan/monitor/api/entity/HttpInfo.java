package io.moyada.sharingan.monitor.api.entity;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class HttpInfo extends MethodInfo {

    private HttpType type;

    private String[] param;

    private String[] header;

    private ContentType contentType;

    public HttpInfo(int appId, int serviceId, String name) {
        super(appId, serviceId, name);
    }

    public HttpType getType() {
        return type;
    }

    public void setType(HttpType type) {
        this.type = type;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }
}
