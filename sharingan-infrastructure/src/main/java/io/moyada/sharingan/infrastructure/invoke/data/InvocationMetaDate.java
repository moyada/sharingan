package io.moyada.sharingan.infrastructure.invoke.data;

/**
 * 调用信息
 */
public abstract class InvocationMetaDate {

    /**
     * 服务名
     */
    private String applicationName;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 方法名
     */
    private String methodName;

    public InvocationMetaDate(String applicationName, String serviceName, String methodName) {
        this.applicationName = applicationName;
        this.serviceName = serviceName;
        this.methodName = methodName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }
}
