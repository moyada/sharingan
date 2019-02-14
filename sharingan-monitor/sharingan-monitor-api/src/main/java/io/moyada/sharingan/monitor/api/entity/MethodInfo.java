package io.moyada.sharingan.monitor.api.entity;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class MethodInfo {

    private int appId;

    private int serviceId;

    private String name;

    public MethodInfo(int appId, int serviceId, String name) {
        this.appId = appId;
        this.serviceId = serviceId;
        this.name = name;
    }

    public int getAppId() {
        return appId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }
}
