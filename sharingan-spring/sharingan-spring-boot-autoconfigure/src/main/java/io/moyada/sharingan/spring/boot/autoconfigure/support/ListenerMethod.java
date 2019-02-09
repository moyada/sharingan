package io.moyada.sharingan.spring.boot.autoconfigure.support;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ListenerMethod {

    /**
     * 方法归属域
     */
    private String domain;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class[] paramTypes;

    /**
     * 序列化方式
     */
    private String serializationType;

    /**
     * 代理参数
     */
    private List<ProxyField> proxyParams;

    /**
     * 代理逻辑先执行
     */
    private boolean proxyBefore = false;

    /**
     * 代理逻辑后执行
     */
    private boolean proxyAfter = false;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
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
