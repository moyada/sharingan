package io.moyada.sharingan.domain.metadada;

import java.lang.invoke.MethodHandle;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ClassData {

    /**
     * 接口类型
     */
    private Class classType;

    /**
     * 方法参数类型
     */
    private Class[] paramTypes;

    /**
     * 方法返回类型
     */
    private Class returnType;

    /**
     * 方法具柄
     */
    private MethodHandle methodHandle;

    public ClassData(Class classType, Class[] paramTypes, Class returnType, MethodHandle methodHandle) {
        this.classType = classType;
        this.paramTypes = paramTypes;
        this.returnType = returnType;
        this.methodHandle = methodHandle;
    }

    public Class getClassType() {
        return classType;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public Class getReturnType() {
        return returnType;
    }

    public MethodHandle getMethodHandle() {
        return methodHandle;
    }
}
