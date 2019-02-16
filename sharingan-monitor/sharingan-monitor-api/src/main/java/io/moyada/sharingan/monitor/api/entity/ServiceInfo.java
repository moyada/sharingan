package io.moyada.sharingan.monitor.api.entity;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ServiceInfo {

    private int appId;

    private String name;

    private Protocol protocol;

    private Class classType;

    public ServiceInfo(int appId, String name) {
        this.appId = appId;
        this.name = name;
    }

    public int getAppId() {
        return appId;
    }

    public String getName() {
        return name;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Class getClassType() {
        return classType;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }
}
