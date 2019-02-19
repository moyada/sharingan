package io.moyada.sharingan.domain.metadada;


import io.moyada.sharingan.infrastructure.module.Dependency;
import io.moyada.sharingan.infrastructure.util.ConvertUtil;
import io.moyada.sharingan.infrastructure.util.StringUtil;

/**
 * 项目信息
 * @author xueyikang
 * @create 2018-04-14 02:22
 */
public class AppData {

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
     * 项目依赖 {@link io.moyada.sharingan.domain.metadada.AppData} appId,appId...
     */
    private String dependencies;

    private AppData() {
    }

    public AppData(Integer id, String name, String groupId, String artifactId, String version, String url, String dependencies) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.url = url;
        this.dependencies = dependencies;
    }

    public Dependency getDependency() {
        return new Dependency(groupId, artifactId, version, url);
    }

    public int[] getDependencies() {
        if (StringUtil.isEmpty(dependencies)) {
            return null;
        }

        String[] split = dependencies.split(",");
        if (split.length == 0) {
            return null;
        }
        return ConvertUtil.convertInt(split);
    }

    private void setId(Integer id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    private void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    private void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }
}
