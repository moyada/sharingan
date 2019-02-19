package io.moyada.sharingan.domain.metadada;

import io.moyada.sharingan.infrastructure.invoke.data.InvocationMetaDate;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class InvokeData<T extends InvocationMetaDate> {

    /**
     * 方法名
     */
    private String methodName;

    private ServiceData serviceData;

    InvokeData() {
    }

    public InvokeData(String methodName) {
        this.methodName = methodName;
    }

    private void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setServiceData(ServiceData serviceData) {
        this.serviceData = serviceData;
    }

    public String getMethodName() {
        return methodName;
    }

    public ServiceData getServiceData() {
        return serviceData;
    }

    public abstract T getInvocation();
}
