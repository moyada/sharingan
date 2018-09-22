package cn.moyada.sharingan.module.fetch;


import cn.moyada.sharingan.common.enums.HttpScheme;
import cn.moyada.sharingan.common.exception.InitializeInvokerException;
import cn.moyada.sharingan.common.support.SimpleHttpClient;
import cn.moyada.sharingan.common.utils.JsonUtil;
import cn.moyada.sharingan.common.utils.StringUtil;
import cn.moyada.sharingan.module.Assert;
import cn.moyada.sharingan.module.Dependency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 获取nexus下最近jar包
 * @author xueyikang
 * @create 2018-04-27 15:00
 */
@Component
public class Nexus3Fetch implements DependencyFetch {

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

    public Nexus3Fetch(@Value("${maven.host}") String host) {
        if (StringUtil.isEmpty(host)) {
            throw new NullPointerException("cannot find maven.host properties.");
        }
        if (!HttpScheme.checkout(host)) {
            throw new InitializeInvokerException("maven.host must use http or https protocol.");
        }

        if(!host.endsWith("/")) {
            host = host + "/";
        }

        this.DOWNLOAD_URL = host + "repository/";
        this.MAVEN_URL = host + "service/extdirect";
        this.httpClient = new SimpleHttpClient();
    }

    /**
     * 获取jar包路径
     * @param dependency 依赖
     * @return
     */
    @Override
    public String getJarUrl(Dependency dependency) {
        String lastDependency = getLastDependency(dependency);
        Assert buildAssert = buildAssert(lastDependency);
        if(null == buildAssert) {
            return null;
        }
        String jarAssert = getAssert(dependency, buildAssert);
        jarAssert = getJarUrl(jarAssert);
        if(null == jarAssert) {
            return null;
        }
        return DOWNLOAD_URL + buildAssert.getRepositoryName() + "/" + jarAssert;
    }

    /**
     * 解析结果获取jar包路径
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    private static String getJarUrl(String str) {
        Map<String, Object> response = JsonUtil.toMap(str, String.class, Object.class);
        if(null == response || !response.containsKey("result")) {
            return null;
        }
        Map<String, Object> result = (Map<String, Object>) response.get("result");
        List<Object> data = (List<Object>) result.get("data");
        if(null == data || data.isEmpty()) {
            return null;
        }
        Object name;
        String jarName;
        for(Object obj : data) {
            result = (Map<String, Object>) obj;
            name = result.get("name");
            if(null == name) {
                continue;
            }
            jarName = name.toString();
            if(jarName.endsWith(".jar") && !jarName.endsWith("sources.jar")) {
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
     * @param assertInfo
     * @return
     */
    private String getAssert(Dependency dependency, Assert assertInfo) {
        String data = assertQuest(dependency, assertInfo);
        return httpClient.post(MAVEN_URL, data);
    }

    /**
     * 封装jar包请求信息
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    private Assert buildAssert(String str) {
        Map<String, Object> response = JsonUtil.toMap(str, String.class, Object.class);
        if(null == response || !response.containsKey("result")) {
            return null;
        }
        Map<String, Object> result = (Map<String, Object>) response.get("result");
        List<Object> data = (List<Object>) result.get("data");
        if(null == data || data.isEmpty()) {
            return null;
        }
        result = (Map<String, Object>) data.get(0);
        Assert assertInfo = new Assert();
        assertInfo.setComponentId(result.get("id").toString());
        assertInfo.setRepositoryName(result.get("repositoryName").toString());
        assertInfo.setComponentVersion(result.get("version").toString());
        return assertInfo;
    }

    /**
     * 格式化列表获取链接
     * @param dependency
     * @return
     */
    private String listQuest(Dependency dependency) {
        String version = dependency.getVersion();
        if(null == version) {
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
     * @param assertInfo
     * @return
     */
    private String assertQuest(Dependency dependency, Assert assertInfo) {
        return String.format(ASSERT_QUEST,
                assertInfo.getRepositoryName(),
                assertInfo.getComponentId(),
                dependency.getArtifactId(),
                dependency.getGroupId(),
                assertInfo.getComponentVersion());
    }

    public static void main(String[] args) {
        DependencyFetch dependencyFetch = new Nexus3Fetch("http://127.0.0.1:8081");

        Dependency dependency = new Dependency();
        dependency.setGroupId("cn.moyada");
        dependency.setArtifactId("dubbo-test-api");
        String url = dependencyFetch.getJarUrl(dependency);
        System.out.println(url);
    }
}
