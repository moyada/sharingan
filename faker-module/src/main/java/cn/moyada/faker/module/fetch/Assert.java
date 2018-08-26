package cn.moyada.faker.module.fetch;

/**
 * Nexus Maven依赖信息
 * @author xueyikang
 * @create 2018-04-27 15:40
 */
public class Assert {

    /**
     * 版本信息, SNAPSHOT
     */
    private String repositoryName;

    /**
     * 依赖Id
     */
    private String componentId;

    /**
     * 依赖版本
     */
    private String componentVersion;

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
        this.componentVersion = componentVersion;
    }
}
