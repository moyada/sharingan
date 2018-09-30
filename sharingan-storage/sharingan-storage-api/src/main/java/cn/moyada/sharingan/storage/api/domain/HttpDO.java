package cn.moyada.sharingan.storage.api.domain;

import java.io.Serializable;

/**
 * http请求信息
 * @author xueyikang
 * @since 1.0
 **/
public class HttpDO implements Serializable {

    private static final long serialVersionUID = -6198595239987970153L;

    /**
     * 方法编号
     */
    private Integer id;

    /**
     * 项目编号
     */
    private Integer appId;

    /**
     * 服务编号
     */
    private Integer serviceId;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 请求类别, GET\POST\PUT\DELETE
     */
    private String methodType;

    /**
     * 参数
     */
    private String param;

    /**
     * 头信息
     */
    private String header;

    /**
     * 表达式
     */
    private String expression;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
