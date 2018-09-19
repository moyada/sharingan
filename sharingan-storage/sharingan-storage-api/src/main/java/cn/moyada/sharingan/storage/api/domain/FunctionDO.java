package cn.moyada.sharingan.storage.api.domain;


import java.io.Serializable;

/**
 * 方法调用信息
 * @author xueyikang
 * @create 2017-12-13 14:30
 */
public class FunctionDO implements Serializable {

    private static final long serialVersionUID = -2885833434070073759L;

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
     * 接口路径名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 返回值类型
     */
    private String returnType;

    /**
     * 默认参数表达式
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
