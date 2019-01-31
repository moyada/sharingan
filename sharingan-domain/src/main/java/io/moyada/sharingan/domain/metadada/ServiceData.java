package io.moyada.sharingan.domain.metadada;

/**
 * 服务信息
 */
public class ServiceData {

    /**
     * 服务编号
     */
    private Integer id;

    /**
     * 服务名
     */
    private String name;

    /**
     * RPC协议
     */
    private String protocol;

    /**
     * RPC方式
     */
    private String protocolType;

    /**
     * 类名
     */
    private String className;

    private AppData appData;

    public boolean isHttp() {
        if (null == protocolType) {
            return false;
        }
        return protocolType.equalsIgnoreCase("HTTP");
    }

    public void setAppData(AppData appData) {
        this.appData = appData;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    private void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    private void setClassName(String className) {
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
