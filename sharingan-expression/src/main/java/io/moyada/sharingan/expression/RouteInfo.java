package io.moyada.sharingan.expression;

/**
 * 路由信息
 */
public class RouteInfo {

    /**
     * 数据项目编号
     */
    private int appId;

    /**
     * 项目数据领域
     */
    private String domain;

    public RouteInfo(int appId, String domain) {
        this.appId = appId;
        this.domain = domain;
    }

    public int getAppId() {
        return appId;
    }

    public String getDomain() {
        return domain;
    }
}
