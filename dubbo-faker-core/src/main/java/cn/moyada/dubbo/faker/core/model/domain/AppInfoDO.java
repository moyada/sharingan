package cn.moyada.dubbo.faker.core.model.domain;

/**
 * @author xueyikang
 * @create 2018-04-14 02:22
 */
public class AppInfoDO {

    private Integer appId;

    private String appName;

    private String groupId;

    private String artifactId;

    private String version;

    private String url;

    public String buildUrl(String host) {
        return host + "/" +
                this.groupId + "/" +
                this.artifactId + "-" +
                this.version + ".jar";
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
