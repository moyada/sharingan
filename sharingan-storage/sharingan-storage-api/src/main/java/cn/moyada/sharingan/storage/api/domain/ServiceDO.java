package cn.moyada.sharingan.storage.api.domain;

import java.io.Serializable;

/**
 * 服务信息
 */
public class ServiceDO implements Serializable {

    private static final long serialVersionUID = 4997626017364083642L;

    /**
     * 服务编号
     */
    private Integer id;

    /**
     * 项目编号
     */
    private Integer appId;

    /**
     * 服务名
     */
    private String name;

    /**
     * RPC协议
     */
    private String protocol;

    /**
     * 类名
     */
    private String className;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
