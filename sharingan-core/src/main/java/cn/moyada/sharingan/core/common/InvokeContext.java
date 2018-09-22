package cn.moyada.sharingan.core.common;

import cn.moyada.sharingan.module.Dependency;
import cn.moyada.sharingan.module.InvokeMetaData;

/**
 * 调用任务上下文
 */
public class InvokeContext {

    /**
     * 测试编码
     */
    private String fakerId;

    /**
     * 项目编号
     */
    private Integer appId;

    /**
     * 服务编号
     */
    private Integer serviceId;

    /**
     * 方法编号
     */
    private Integer funcId;

    /**
     * 参数表达式
     */
    private String[] expression;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 版本信息
     */
    private Dependency dependency;

    /**
     * 请求方法信息
     */
    private InvokeMetaData invokeMetaData;

    public String getFakerId() {
        return fakerId;
    }

    public void setFakerId(String fakerId) {
        this.fakerId = fakerId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getFuncId() {
        return funcId;
    }

    public void setFuncId(Integer funcId) {
        this.funcId = funcId;
    }

    public String[] getExpression() {
        return expression;
    }

    public void setExpression(String[] expression) {
        this.expression = expression;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    public InvokeMetaData getInvokeMetaData() {
        return invokeMetaData;
    }

    public void setInvokeMetaData(InvokeMetaData invokeMetaData) {
        this.invokeMetaData = invokeMetaData;
    }
}
