package cn.moyada.sharingan.instrument.boost.common;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ProxyClass {

    private Class clazz;

    private List<ProxyMethod> proxyMethods;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<ProxyMethod> getProxyMethods() {
        return proxyMethods;
    }

    public void setProxyMethods(List<ProxyMethod> proxyMethods) {
        this.proxyMethods = proxyMethods;
    }
}
