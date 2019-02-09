package io.moyada.sharingan.spring.boot.autoconfigure.support;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ListenerInfo {

    private Collection<ListenerMethod> listenerMethods;

    public Collection<ListenerMethod> getListenerMethods() {
        return listenerMethods;
    }

    public void setListenerMethods(Collection<ListenerMethod> listenerMethods) {
        this.listenerMethods = listenerMethods;
    }
}
