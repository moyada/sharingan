package cn.xueyikang.dubbo.faker.core.model;

import java.lang.invoke.MethodHandle;

/**
 * @author xueyikang
 * @create 2017-12-31 16:00
 */
public class FakerProxy {

    private Class<?>[] paramTypes;

    private MethodHandle methodHandle;

    private Object[] service;

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public MethodHandle getMethodHandle() {
        return methodHandle;
    }

    public void setMethodHandle(MethodHandle methodHandle) {
        this.methodHandle = methodHandle;
    }

    public Object[] getService() {
        return service;
    }

    public void setService(Object[] service) {
        this.service = service;
    }
}
