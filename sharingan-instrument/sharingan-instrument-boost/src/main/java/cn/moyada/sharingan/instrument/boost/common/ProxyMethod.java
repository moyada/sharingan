package cn.moyada.sharingan.instrument.boost.common;

import cn.moyada.sharingan.monitor.api.entity.SerializationType;

import java.util.List;

/**
 * 方法代理信息
 * @author xueyikang
 * @since 1.0
 **/
public class ProxyMethod {

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class[] paramTypes;

    /**
     * 方法归属域
     */
    private String domain;

    /**
     * 协议类型
     */
    private String protocol;

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

    public String getSerializationType() {
        return SerializationType.class.getName() + "." + serializationType;
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
