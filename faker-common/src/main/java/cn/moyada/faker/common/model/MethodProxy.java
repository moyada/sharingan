package cn.moyada.faker.common.model;

import java.lang.invoke.MethodHandle;

/**
 * 请求代理信息
 * @author xueyikang
 * @create 2017-12-31 16:00
 */
public class MethodProxy {

    /**
     * 请求编号
     */
    private String fakerId;

    /**
     * 请求参数
     */
    private Object[] values;

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

    public String getFakerId() {
        return fakerId;
    }

    public void setFakerId(String fakerId) {
        this.fakerId = fakerId;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

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
