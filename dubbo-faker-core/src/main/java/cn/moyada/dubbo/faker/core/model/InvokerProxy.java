package cn.moyada.dubbo.faker.core.model;

import java.lang.invoke.MethodHandle;

/**
 * @author xueyikang
 * @create 2018-04-03 14:52
 */
public class InvokerProxy {

    /**
     * 方法具柄
     */
    private MethodHandle methodHandle;

    /**
     * 调用接口
     */
    private Object service;

    public InvokerProxy(MethodHandle methodHandle, Object service) {
        this.methodHandle = methodHandle;
        this.service = service;
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