package io.moyada.sharingan.domain.expression;

import java.util.Collections;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class HttpInfo {

    private Map<String, String> param;
    private Map<String, String> header;

    private String body;

    public HttpInfo(Map<String, String> param, Map<String, String> header, String body) {
        this.param = null == param ? Collections.emptyMap() : param;
        this.header = null == header ? Collections.emptyMap() : header;
        this.body = body;
    }

    public boolean isEmpty() {
        if (!param.isEmpty()) {
            return false;
        }
        if (!header.isEmpty()) {
            return false;
        }

        return body == null;
    }

    public String[] getValue() {
        int size = param.size() + header.size();
        String[] values = new String[null == body ? size : size + 1];

        int index = 0;
        if (null != body) {
            values[index++] = body;
        }

        for (String value : param.values()) {
            values[index++] = value;
        }
        for (String value : header.values()) {
            values[index++] = value;
        }
        return values;
    }

    public String[] getParam() {
        return param.keySet().toArray(new String[0]);
    }

    public String[] getHeader() {
        return header.keySet().toArray(new String[0]);
    }

    public String getBody() {
        return body;
    }
}
