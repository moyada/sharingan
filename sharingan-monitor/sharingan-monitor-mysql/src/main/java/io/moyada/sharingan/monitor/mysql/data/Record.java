package io.moyada.sharingan.monitor.mysql.data;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Record {

    private Integer appId;

    private String domain;

    private String paramValue;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
