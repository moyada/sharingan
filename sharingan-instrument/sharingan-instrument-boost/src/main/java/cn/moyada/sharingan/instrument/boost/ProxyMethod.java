package cn.moyada.sharingan.instrument.boost;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ProxyMethod {

    private String methodName;

    private String domain;

    private String protocol;

    private List<ProxyField> proxyParams;

    private boolean proxyBefore = false;

    private boolean proxyAfter = false;

    public Class[] getParamTypes() {
        int size = proxyParams.size();
        Class[] paramTypes = new Class[size];
        int index = 0;
        for (ProxyField proxyField : proxyParams) {
            paramTypes[index++] = proxyField.getParamType();
        }
        return paramTypes;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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
