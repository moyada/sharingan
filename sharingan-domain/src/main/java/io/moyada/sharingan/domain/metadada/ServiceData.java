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
    private Protocol protocol;

    /**
     * 类名
     */
    private String className;

    private AppData appData;

    private ServiceData() {
    }

    public ServiceData(Integer id, String name, Protocol protocol, String className) {
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.className = className;
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

    private void setProtocol(Protocol protocol) {
        this.protocol = protocol;
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

    public Protocol getProtocol() {
        return protocol;
    }

    public String getClassName() {
        return className;
    }

    public AppData getAppData() {
        return appData;
    }
}
