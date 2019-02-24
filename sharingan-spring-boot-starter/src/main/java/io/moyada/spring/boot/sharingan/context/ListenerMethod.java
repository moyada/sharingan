package io.moyada.spring.boot.sharingan.context;

import io.moyada.sharingan.monitor.api.entity.ContentType;
import io.moyada.sharingan.monitor.api.entity.HttpType;
import io.moyada.spring.boot.sharingan.annotation.HttpMethod;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ListenerMethod {

    private boolean needRegister = false;

    /**
     * 方法归属域
     */
    private String domain;

    /**
     * 方法名
     */
    private String methodName;

    private HttpData httpData;

    /**
     * 参数类型
     */
    private Class[] paramTypes;

    /**
     * 返回类型
     */
    private Class returnType;

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

    public ListenerMethod(Method method) {
        this.methodName = method.getName();
        this.paramTypes = method.getParameterTypes();
        this.returnType = method.getReturnType();
    }

    public void setHttpData(HttpMethod annotation) {
        if (null == annotation) {
            return;
        }
        String name = annotation.value();
        if (name.isEmpty()) {
            name = "/" + methodName;
        } else if (name.charAt(0) != '/') {
            name = "/" + name;
        }
        HttpType type = annotation.type();
        ContentType contentType = annotation.contentType();
        String[] param = annotation.param();
        String[] header = annotation.header();
        this.httpData = new HttpData(name, type, contentType, param, header);
    }

    public void setNeedRegister() {
        this.needRegister = true;
    }

    public boolean isNeedRegister() {
        return needRegister;
    }

    public HttpData getHttpData() {
        return httpData;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public Class getReturnType() {
        return returnType;
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
