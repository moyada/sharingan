package io.moyada.sharingan.monitor.api.entity;

/**
 * @author xueyikang
 * @since 1.0
 **/
public enum ContentType {

    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    TEXT_XML("text/xml"),
    ;
    private String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }}
