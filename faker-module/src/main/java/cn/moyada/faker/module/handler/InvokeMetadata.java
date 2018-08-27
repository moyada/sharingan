package cn.moyada.faker.module.handler;

import java.lang.invoke.MethodHandle;

/**
 * 方法请求信息
 * @author xueyikang
 * @create 2017-12-31 16:00
 */
public class InvokeMetadata {

    /**
     * 方法参数类型
     */
    private Class classType;

    /**
     * 方法具柄
     */
    private MethodHandle methodHandle;

    /**
     * 方法参数类型
     */
    private Class[] paramTypes;

    /**
     * 方法返回类型
     */
    private Class returnType;

    public Class getClassType() {
        return classType;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }

    public MethodHandle getMethodHandle() {
        return methodHandle;
    }

    public void setMethodHandle(MethodHandle methodHandle) {
        this.methodHandle = methodHandle;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }
}
