package io.moyada.sharingan.web.vo;

import java.io.Serializable;

/**
 * @author xueyikang
 * @create 2018-01-01 11:35
 */
public class SelectVO implements Serializable {

    private static final long serialVersionUID = -7750004293151410471L;

    private Object key;

    private String value;

    private String extra;

    private String header;

    private String body;

    public SelectVO(Object key, String value) {
        this.key = key;
        this.value = value;
    }

    public SelectVO(Object key, String value, String extra) {
        this.key = key;
        this.value = value;
        this.extra = extra;
    }

    public SelectVO(Object key, String value, String extra, String header, String body) {
        this.key = key;
        this.value = value;
        this.extra = extra;
        this.header = header;
        this.body = body;
    }

    public Object getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getExtra() {
        return extra;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
