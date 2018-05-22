package cn.moyada.dubbo.faker.core.loader;

import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 获取nexus下最近jar包
 * @author xueyikang
 * @create 2018-04-27 15:00
 */
@Component
public class FetchLastJar {

    private final HttpClient instance;

    private final String DOWNLOAD_URL;
    private final String MAVEN_URL;

    // 列表链接
    private static String LIST_QUEST = "{\"action\":\"coreui_Search\",\"method\":\"read\",\"data\":[{\"page\":1,\"start\":0,\"limit\":1,\"sort\":[{\"property\":\"version\",\"direction\":\"DESC\"}],\"filter\":[" +
            "{\"property\":\"format\",\"value\":\"mavenVersion\"}," +
            "{\"property\":\"attributes.mavenVersion.groupId\",\"value\":\"%s\"}," +
            "{\"property\":\"attributes.mavenVersion.artifactId\",\"value\":\"%s\"}," +
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

    public FetchLastJar(@Value("${maven.host}") String host, @Value("${maven.version}") String version) {
        if(null == host) {
            throw new NullPointerException("unknow maven.host properties.");
        }
        if(null == version) {
            throw new NullPointerException("unknow maven.version properties.");
        }

        if(!host.startsWith("https://") && !host.startsWith("http://")) {
            throw new IllegalArgumentException("maven host is illegal.");
        }

        if(host.endsWith("/")) {
            this.DOWNLOAD_URL = host + "repository/snapshots/";
            this.MAVEN_URL = host + "service/extdirect";
        }
        else {
            this.DOWNLOAD_URL = host + "/repository/snapshots/";
            this.MAVEN_URL = host + "/service/extdirect";
        }

        LIST_QUEST = LIST_QUEST.replace("mavenVersion", version);
        this.instance = HttpClientBuilder.create().build();
    }

    /**
     * 获取jar包路径
     * @param dependency 依赖
     * @return
     */
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
     * 获取最近更新依赖
     * @param dependency
     * @return
     */
    private String getLastDependency(Dependency dependency) {
        HttpPost httpPost = new HttpPost(MAVEN_URL);
        String data = ListQuest(dependency);
        httpPost.setEntity(new StringEntity(data, StandardCharsets.UTF_8));
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        return request(httpPost);
    }

    /**
     * 获取jar包链接
     * @param dependency
     * @param assertInfo
     * @return
     */
    private String getAssert(Dependency dependency, Assert assertInfo) {
        HttpPost httpPost = new HttpPost(MAVEN_URL);
        String data = AssertQuest(dependency, assertInfo);
        httpPost.setEntity(new StringEntity(data, StandardCharsets.UTF_8));
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        return request(httpPost);
    }

    /**
     * 发送请求
     * @param request
     * @return
     */
    private String request(HttpUriRequest request) {
        String req;
        try {
            HttpResponse execute = instance.execute(request);
            req = EntityUtils.toString(execute.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            req = null;
        }
        return req;
    }

    /**
     * 封装jar包请求信息
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Assert buildAssert(String str) {
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
//        return MessageFormat.format(LIST_QUEST,
//                dependency.getGroupId(),
//                dependency.getArtifactId(),
//                version);
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
//        return MessageFormat.format(ASSERT_QUEST,
//                assertInfo.getRepositoryName(),
//                assertInfo.getComponentId(),
//                dependency.getArtifactId(),
//                dependency.getGroupId(),
//                assertInfo.getComponentVersion());
        return String.format(ASSERT_QUEST,
                assertInfo.getRepositoryName(),
                assertInfo.getComponentId(),
                dependency.getArtifactId(),
                dependency.getGroupId(),
                assertInfo.getComponentVersion());
    }

    public static void main(String[] args) {
        String host = "https://repo.souche-inc.com";
        String version = "maven2";
        Dependency dependency = new Dependency();
        dependency.setGroupId("com.souche");
        dependency.setArtifactId("car-model-api");
        dependency.setVersion("1.2.0-SNAPSHOT");
        FetchLastJar fetchLastJar = new FetchLastJar(host, version);
        String jarUrl = fetchLastJar.getJarUrl(dependency);
        System.out.println(jarUrl);
    }
}
