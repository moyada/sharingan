package cn.moyada.sharingan.core.support;

/**
 * 路由信息
 */
public class RouteInfo {

    /**
     * 目标表达式
     */
    private String target;

    /**
     * 数据项目编号
     */
    private int appId;

    /**
     * 项目数据领域
     */
    private String domain;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "RouteInfo{" +
                "target='" + target + '\'' +
                ", appId=" + appId +
                ", domain='" + domain + '\'' +
                '}';
    }
}
