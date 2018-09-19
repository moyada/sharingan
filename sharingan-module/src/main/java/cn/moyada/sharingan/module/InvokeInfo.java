package cn.moyada.sharingan.module;

/**
 * 调用方法
 * @author xueyikang
 * @create 2018-08-27 11:03
 */
public class InvokeInfo {

    /**
     * 类名
     */
    private String classType;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private String paramType;

    /**
     * 返回值
     */
    private String returnType;

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
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
}
