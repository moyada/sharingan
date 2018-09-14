package cn.moyada.faker.module.fetch;


import cn.moyada.faker.common.utils.JsonUtil;
import cn.moyada.faker.common.utils.StringUtil;
import cn.moyada.faker.module.Assert;
import cn.moyada.faker.module.Dependency;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 获取nexus下最近jar包
 * @author xueyikang
 * @create 2018-04-27 15:00
 */
@Component
public class Maven2LastJarFetch extends AbstractFetchLastJar implements DependencyFetch {

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

    public Maven2LastJarFetch(@Value("${maven.host}") String host) {
        if(StringUtil.isEmpty(host)) {
            throw new NullPointerException("unknow maven.host properties.");
        }

        if(host.endsWith("/")) {
            this.DOWNLOAD_URL = host + "repository/snapshots/";
            this.MAVEN_URL = host + "service/extdirect";
        }
        else {
            this.DOWNLOAD_URL = host + "/repository/snapshots/";
            this.MAVEN_URL = host + "/service/extdirect";
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
        Assert buildAssert = buildAssert(lastDependency);
        if(null == buildAssert) {
            return null;
        }
        String jarAssert = getAssert(dependency, buildAssert);
        jarAssert = getJarUrl(jarAssert);
        if(null == jarAssert) {
            return null;
        }
        return DOWNLOAD_URL + jarAssert;
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
        HttpPost httpPost = getHttpPost(MAVEN_URL);
        String data = ListQuest(dependency);
        httpPost.setEntity(new StringEntity(data, StandardCharsets.UTF_8));
        return request(httpPost);
    }

    /**
     * 获取jar包链接
     * @param dependency
     * @param assertInfo
     * @return
     */
    private String getAssert(Dependency dependency, Assert assertInfo) {
        HttpPost httpPost = getHttpPost(MAVEN_URL);
        String data = AssertQuest(dependency, assertInfo);
        httpPost.setEntity(new StringEntity(data, StandardCharsets.UTF_8));
        return request(httpPost);
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
    private String ListQuest(Dependency dependency) {
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
    private String AssertQuest(Dependency dependency, Assert assertInfo) {
        return String.format(ASSERT_QUEST,
                assertInfo.getRepositoryName(),
                assertInfo.getComponentId(),
                dependency.getArtifactId(),
                dependency.getGroupId(),
                assertInfo.getComponentVersion());
    }
}
