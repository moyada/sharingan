package io.moyada.sharingan.monitor.api.entity;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class FunctionInfo extends MethodInfo {

    /**
     * 接口类型
     */
    private Class<?> classType;

    /**
     * 参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 返回类型
     */
    private Class<?> returnType;

    public FunctionInfo(int appId, int serviceId, String name) {
        super(appId, serviceId, name);
    }

    public Class<?> getClassType() {
        return classType;
    }

    public void setClassType(Class<?> classType) {
        this.classType = classType;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
