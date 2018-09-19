package cn.moyada.sharingan.storage.api.domain;

import java.io.Serializable;

/**
 * 项目信息
 * @author xueyikang
 * @create 2018-04-14 02:22
 */
public class AppDO implements Serializable {

    private static final long serialVersionUID = 198121162376459473L;

    /**
     * 项目编号
     */
    private Integer id;

    /**
     * 项目名
     */
    private String name;

    /**
     * 依赖信息
     */
    private String groupId;

    /**
     * 依赖信息
     */
    private String artifactId;

    /**
     * 指定版本
     */
    private String version;

    /**
     * 指定jar包路径
     */
    private String url;

    /**
     * 项目依赖{@see cn.moyada.faker.storage.api.domain.AppDO} appId,appId...
     */
    private String dependencies;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }
}
