package io.moyada.sharingan.domain.metadada;

import io.moyada.sharingan.infrastructure.invoke.data.ClassInvocation;

import java.lang.invoke.MethodHandle;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ClassData extends InvokeData<ClassInvocation> {

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

    public ClassData(String methodName, Class classType, Class[] paramTypes, Class returnType, MethodHandle methodHandle) {
        super(methodName);
        this.classType = classType;
        this.paramTypes = paramTypes;
        this.returnType = returnType;
        this.methodHandle = methodHandle;
    }

    @Override
    public ClassInvocation getInvocation() {
        ServiceData serviceData = getServiceData();
        AppData appData = serviceData.getAppData();
        return new ClassInvocation(appData.getName(), serviceData.getName(), getMethodName(),
                getClassType(), getParamTypes(), getMethodHandle());
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
