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

    private MethodData() {
    }

    public MethodData(Integer id, String className, String methodName, String paramType, String returnType, String expression) {
        this.id = id;
        this.className = className;
        this.methodName = methodName;
        this.paramType = paramType;
        this.returnType = returnType;
        this.expression = expression;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    private void setClassName(String className) {
        this.className = className;
    }

    private void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    private void setParamType(String paramType) {
        this.paramType = paramType;
    }

    private void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    private void setExpression(String expression) {
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
}
