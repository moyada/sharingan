package io.moyada.sharingan.infrastructure.invoke.data;

import java.lang.invoke.MethodHandle;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ClassInvocation extends InvocationMetaDate {

    /**
     * 接口类型
     */
    private Class<?> classType;

    private Class<?>[] paramTypes;

    /**
     * 方法具柄
     */
    private MethodHandle methodHandle;

    public ClassInvocation(String applicationName, String serviceName, String methodName,
                           Class<?> classType, Class<?>[] paramTypes, MethodHandle methodHandle) {
        super(applicationName, serviceName, methodName);
        this.classType = classType;
        this.paramTypes = paramTypes;
        this.methodHandle = methodHandle;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public MethodHandle getMethodHandle() {
        return methodHandle;
    }
}
