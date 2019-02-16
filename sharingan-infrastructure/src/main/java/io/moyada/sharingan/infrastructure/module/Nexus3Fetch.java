package io.moyada.sharingan.infrastructure.module;

import io.moyada.sharingan.infrastructure.config.MavenConfig;
import io.moyada.sharingan.infrastructure.enums.HttpScheme;
import io.moyada.sharingan.infrastructure.exception.InitializeInvokerException;
import io.moyada.sharingan.infrastructure.support.SimpleHttpClient;
import io.moyada.sharingan.infrastructure.util.JsonUtil;
import io.moyada.sharingan.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 获取nexus下最近jar包
 * @author xueyikang
 * @create 2018-04-27 15:00
 */
@Deprecated
//@Component
public class Nexus3Fetch implements ArtifactFetch {

    private final SimpleHttpClient httpClient;

    private final String DOWNLOAD_URL;
    private final String MAVEN_URL;

    // 列表链接
    private static String LIST_QUEST = "{\"action\":\"coreui_Search\",\"method\":\"read\",\"data\":[{\"page\":1,\"start\":0,\"limit\":1,\"sort\":[{\"property\":\"version\",\"direction\":\"DESC\"}],\"filter\":[" +
            "{\"property\":\"format\",\"value\":\"maven2\"}," +
            "{\"property\":\"attributes.maven2.groupId\",\"value\":\"%s\"}," +
            "{\"property\":\"attributes.maven2.artifactId\",\"value\":\"%s\"}," +
            "{property: \"attributes.maven2.baseVersion\", value: %s}" +
            "]}],\"type\":\"rpc\",\"tid\":0}";

    // 依赖链接
    private static final String ASSERT_QUEST = "[{\"action\":\"coreui_Component\",\"method\":\"readComponentAssets\",\"data\":[{\"page\":1,\"start\":0,\"limit\":25,\"filter\":[" +
            "{\"property\":\"repositoryName\",\"value\":\"%s\"}," +
            "{\"property\":\"componentId\",\"value\":\"%s\"}," +
            "{\"property\":\"componentName\",\"value\":\"%s\"}," +
            "{\"property\":\"componentGroup\",\"value\":\"%s\"}," +
            "{\"property\":\"componentVersion\",\"value\":\"%s\"}" +
            "]}],\"type\":\"rpc\",\"tid\":0}]";

    public Nexus3Fetch(@Autowired MavenConfig mavenConfig) {
        String host = mavenConfig.getRegistry();
        if (StringUtil.isEmpty(host)) {
            throw new NullPointerException("cannot find maven.host properties.");
        }
        if (!HttpScheme.checkout(host)) {
            throw new InitializeInvokerException("maven.host must use http or https protocol.");
        }

        if (!host.endsWith("/")) {
            host = host + "/";
        }

        this.DOWNLOAD_URL = host + "repository/";
        this.MAVEN_URL = host + "service/extdirect";

        if (mavenConfig.isCredential()) {
            this.httpClient = new SimpleHttpClient(mavenConfig.getUsername(), mavenConfig.getPassword());
        }
        else {
            this.httpClient = new SimpleHttpClient();
        }
    }

    /**
     * 获取jar包路径
     * @param dependency 依赖
     * @return
     */
    @Override
    public String getJarUrl(Dependency dependency) {
        String lastDependency = getLastDependency(dependency);
        if (null == lastDependency) {
            return null;
        }
        Artifact buildArtifact = buildAssert(lastDependency);
        if (null == buildArtifact) {
            return null;
        }
        String jarAssert = getAssert(dependency, buildArtifact);
        if (null == jarAssert) {
            return null;
        }
        jarAssert = getJarUrl(jarAssert);
        if (null == jarAssert) {
            return null;
        }
        return DOWNLOAD_URL + buildArtifact.getRepositoryName() + "/" + jarAssert;
    }

    /**
     * 解析结果获取jar包路径
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    private static String getJarUrl(String str) {
        Map<String, Object> response = JsonUtil.toMap(str, String.class, Object.class);
        if (null == response || !response.containsKey("result")) {
            return null;
        }
        Map<String, Object> result = (Map<String, Object>) response.get("result");
        List<Object> data = (List<Object>) result.get("data");
        if (null == data || data.isEmpty()) {
            return null;
        }
        Object name;
        String jarName;
        for(Object obj : data) {
            result = (Map<String, Object>) obj;
            name = result.get("name");
            if (null == name) {
                continue;
            }
            jarName = name.toString();
            if (jarName.endsWith(".jar") && !jarName.endsWith("sources.jar")) {
                return jarName;
            }
        }
        return null;
    }

    /**
     * 获取最近更新依赖
     * @param dependency
     * @return
     */
    private String getLastDependency(Dependency dependency) {
        String data = listQuest(dependency);
        return httpClient.post(MAVEN_URL, data);
    }

    /**
     * 获取jar包链接
     * @param dependency
     * @param artifact
     * @return
     */
    private String getAssert(Dependency dependency, Artifact artifact) {
        String data = assertQuest(dependency, artifact);
        return httpClient.post(MAVEN_URL, data);
    }

    /**
     * 封装jar包请求信息
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Artifact buildAssert(String str) {
        Map<String, Object> response = JsonUtil.toMap(str, String.class, Object.class);
        if (null == response || !response.containsKey("result")) {
            return null;
        }
        Map<String, Object> result = (Map<String, Object>) response.get("result");
        List<Object> data = (List<Object>) result.get("data");
        if (null == data || data.isEmpty()) {
            return null;
        }
        result = (Map<String, Object>) data.get(0);
        Artifact artifactInfo = new Artifact();
        artifactInfo.setComponentId(result.get("id").toString());
        artifactInfo.setRepositoryName(result.get("repositoryName").toString());
        artifactInfo.setComponentVersion(result.get("version").toString());
        return artifactInfo;
    }

    /**
     * 格式化列表获取链接
     * @param dependency
     * @return
     */
    private String listQuest(Dependency dependency) {
        String version = dependency.getVersion();
        if (null == version) {
            version = "null";
        }
        // 指定版本号
        else {
            version = "\""+ version + "\"";
        }
        return String.format(LIST_QUEST,
                dependency.getGroupId(),
                dependency.getArtifactId(),
                version);
    }

    /**
     * 格式化jar包获取链接
     * @param dependency
     * @param artifact
     * @return
     */
    private String assertQuest(Dependency dependency, Artifact artifact) {
        return String.format(ASSERT_QUEST,
                artifact.getRepositoryName(),
                artifact.getComponentId(),
                dependency.getArtifactId(),
                dependency.getGroupId(),
                artifact.getComponentVersion());
    }
}
