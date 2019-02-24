package io.moyada.spring.boot.sharingan.context;

import io.moyada.sharingan.monitor.api.entity.Protocol;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ListenerInfo {

    private String serviceName;

    private Class classType;

    private Protocol protocol;

    private Collection<ListenerMethod> listenerMethods;

    public ListenerInfo(String serviceName, Class classType, Protocol protocol, Collection<ListenerMethod> listenerMethods) {
        this.classType = classType;
        this.serviceName = serviceName;
        this.protocol = protocol;
        this.listenerMethods = listenerMethods;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Class getClassType() {
        return classType;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Collection<ListenerMethod> getListenerMethods() {
        return listenerMethods;
    }
}
