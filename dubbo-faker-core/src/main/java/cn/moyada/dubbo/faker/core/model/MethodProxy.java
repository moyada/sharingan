package cn.moyada.dubbo.faker.core.model;

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
     * 调用代理
     */
    private InvokerProxy[] invokerProxy;

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

    public InvokerProxy[] getInvokerProxy() {
        return invokerProxy;
    }

    public void setInvokerProxy(InvokerProxy[] invokerProxy) {
        this.invokerProxy = invokerProxy;
    }
}
