package cn.moyada.dubbo.faker.api.domain;

import java.util.Objects;

/**
 * 请求参数
 * @author xueyikang
 * @create 2018-03-30 00:44
 */
public class RealParamDO {

    /**
     * 归属项目
     */
    private Integer appId;

    /**
     * 参数类别
     */
    private String type;

    /**
     * 参数
     */
    private String paramValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealParamDO that = (RealParamDO) o;
        return Objects.equals(appId, that.appId) &&
                Objects.equals(type, that.type) &&
                Objects.equals(paramValue, that.paramValue);
    }

    @Override
    public int hashCode() {

        return Objects.hash(appId, type, paramValue);
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
