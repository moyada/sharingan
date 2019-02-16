package io.moyada.sharingan.domain.metadada;


/**
 * 方法调用信息
 * @author xueyikang
 * @create 2017-12-13 14:30
 */
public class MethodData {

    /**
     * 方法编号
     */
    private Integer id;

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

    private ServiceData serviceData;

    public void setServiceData(ServiceData serviceData) {
        this.serviceData = serviceData;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getParamType() {
        return paramType;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getExpression() {
        return expression;
    }

    public ServiceData getServiceData() {
        return serviceData;
    }
}
