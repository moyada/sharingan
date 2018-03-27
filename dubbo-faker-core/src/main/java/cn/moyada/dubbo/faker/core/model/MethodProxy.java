package cn.moyada.dubbo.faker.core.model;

import java.lang.invoke.MethodHandle;

/**
 * 请求代理信息
 * @author xueyikang
 * @create 2017-12-31 16:00
 */
public class MethodProxy {

    /**
     * 方法参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 方法具柄
     */
    private MethodHandle methodHandle;

    /**
     * 调用接口
     */
    private Object service;

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

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }
}
