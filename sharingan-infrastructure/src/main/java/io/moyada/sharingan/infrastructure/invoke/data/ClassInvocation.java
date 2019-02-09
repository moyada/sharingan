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
    private Class classType;

    /**
     * 方法具柄
     */
    private MethodHandle methodHandle;

    public ClassInvocation(String applicationName, String serviceName, String methodName,
                           Class classType, MethodHandle methodHandle) {
        super(applicationName, serviceName, methodName);
        this.classType = classType;
        this.methodHandle = methodHandle;
    }

    public Class getClassType() {
        return classType;
    }

    public MethodHandle getMethodHandle() {
        return methodHandle;
    }
}
