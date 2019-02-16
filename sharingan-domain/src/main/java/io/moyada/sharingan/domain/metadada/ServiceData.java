package io.moyada.sharingan.domain.metadada;

/**
 * 服务信息
 */
public class ServiceData {

    /**
     * 服务编号
     */
    public Integer id;

    /**
     * 服务名
     */
    public String name;

    /**
     * RPC协议
     */
    public String protocol;

    /**
     * RPC方式
     */
    public String protocolType;

    /**
     * 类名
     */
    public String className;

    public AppData appData;

    public boolean isHttp() {
        if (null == protocolType) {
            return false;
        }
        return protocolType.equalsIgnoreCase("HTTP");
    }

    public void setAppData(AppData appData) {
        this.appData = appData;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public String getClassName() {
        return className;
    }

    public AppData getAppData() {
        return appData;
    }
}
