package io.moyada.sharingan.infrastructure.module;

import java.util.List;
import java.util.Objects;

/**
 * 依赖信息
 * @author xueyikang
 * @create 2018-04-27 15:04
 */
public final class Dependency {

    /**
     * 仓库坐标
     */
    private String groupId;

    /**
     * 仓库坐标
     */
    private String artifactId;

    /**
     * 设置精确版本
     */
    private String version;

    /**
     * 指定jar包路径
     */
    private String url;

    /**
     * 外部依赖
     */
    private List<Dependency> dependencyList;

    public Dependency(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public Dependency(String groupId, String artifactId, String version, String url) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.url = url;
    }

    public void setDependencyList(List<Dependency> dependencyList) {
        this.dependencyList = dependencyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(artifactId, that.artifactId) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version);
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

    public List<Dependency> getDependencyList() {
        return dependencyList;
    }
}
