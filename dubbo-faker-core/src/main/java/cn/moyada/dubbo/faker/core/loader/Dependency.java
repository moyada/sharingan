package cn.moyada.dubbo.faker.core.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 依赖信息
 * @author xueyikang
 * @create 2018-04-27 15:04
 */
public final class Dependency {

    private String groupId;

    private String artifactId;

    /**
     * 设置精确版本
     */
    private String version;

    /**
     * 手动jar包路径
     */
    private String url;

    /**
     * 依赖
     */
    private List<Dependency> dependencyList;

    public Dependency() {
    }

    public Dependency(String groupId, String artifactId, String version, String url) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.url = url;
    }

    public void addDependency(Dependency dependency) {
        if(null == dependencyList) {
            dependencyList = new ArrayList<>();
        }
        dependencyList.add(dependency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(artifactId, that.artifactId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId);
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

    public List<Dependency> getDependencyList() {
        return dependencyList;
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
