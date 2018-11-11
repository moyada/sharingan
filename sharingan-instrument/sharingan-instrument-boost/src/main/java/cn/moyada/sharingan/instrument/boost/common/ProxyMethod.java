package cn.moyada.sharingan.instrument.boost.common;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ProxyMethod {

    private String methodName;

    private Class[] paramTypes;

    private String domain;

    private String protocol;

    private List<ProxyField> proxyParams;

    private boolean proxyBefore = false;

    private boolean proxyAfter = false;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public List<ProxyField> getProxyParams() {
        return proxyParams;
    }

    public void setProxyParams(List<ProxyField> proxyParams) {
        this.proxyParams = proxyParams;
    }

    public boolean isProxyBefore() {
        return proxyBefore;
    }

    public void setProxyBefore(boolean proxyBefore) {
        this.proxyBefore = proxyBefore;
    }

    public boolean isProxyAfter() {
        return proxyAfter;
    }

    public void setProxyAfter(boolean proxyAfter) {
        this.proxyAfter = proxyAfter;
    }
}
