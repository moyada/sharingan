package cn.moyada.sharingan.rpc.api.invoke;

import java.lang.invoke.MethodHandle;

/**
 * 调用信息
 */
public class InvocationMetaDate {

    /**
     * 服务接口类型
     */
    private Class service;

    /**
     * 调用句柄
     */
    private MethodHandle methodHandle;

    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }

    public MethodHandle getMethodHandle() {
        return methodHandle;
    }

    public void setMethodHandle(MethodHandle methodHandle) {
        this.methodHandle = methodHandle;
    }
}
