package cn.moyada.sharingan.module.fetch;


import cn.moyada.sharingan.common.enums.HttpScheme;
import cn.moyada.sharingan.common.exception.InitializeInvokerException;
import cn.moyada.sharingan.common.support.SimpleHttpClient;
import cn.moyada.sharingan.common.utils.JsonUtil;
import cn.moyada.sharingan.common.utils.StringUtil;
import cn.moyada.sharingan.module.Dependency;
import cn.moyada.sharingan.module.MavenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 获取nexus下最近jar包
 * @author xueyikang
 * @create 2018-04-27 15:00
 */
@DependsOn("mavenConfig")
@Component
public class Nexus3RestFetch implements DependencyFetch {

    private final SimpleHttpClient httpClient;

    private final String DOWNLOAD_URL;

    public Nexus3RestFetch(@Autowired MavenConfig mavenConfig) {
        String host = mavenConfig.getHost();
        if (StringUtil.isEmpty(host)) {
            throw new NullPointerException("cannot find maven.host properties.");
        }
        if (!HttpScheme.checkout(host)) {
            throw new InitializeInvokerException("maven.host must use http or https protocol.");
        }

        if(!host.endsWith("/")) {
            host = host + "/";
        }

        this.DOWNLOAD_URL = host + "service/rest/beta/search/assets?";
//        this.DOWNLOAD_URL = host + "service/rest/beta/search/assets/download?";

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
        lastDependency = getJarUrl(lastDependency);
        if (null == lastDependency) {
            return null;
        }
        return lastDependency;
    }

    /**
     * 格式化列表获取链接
     * @param dependency
     * @return
     */
    private String listQuest(Dependency dependency) {
        StringBuilder url = new StringBuilder(DOWNLOAD_URL);

        url.append("maven.groupId=").append(dependency.getGroupId());
        url.append("&maven.artifactId=").append(dependency.getArtifactId());

        String version = dependency.getVersion();
        if (!StringUtil.isEmpty(version)) {
            url.append("&maven.baseVersion=").append(version);
        }

        url.append("&maven.extension=jar&maven.classifier");
        return url.toString();
    }

    /**
     * 获取最近更新依赖
     * @param dependency
     * @return
     */
    private String getLastDependency(Dependency dependency) {
        String url = listQuest(dependency);
        return httpClient.get(url);
    }

    /**
     * 解析结果获取jar包路径
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    private static String getJarUrl(String data) {
        Map<String, Object> result = JsonUtil.toMap(data, String.class, Object.class);
        if (null == result || !result.containsKey("items")) {
            return null;
        }
        List<Object> items = (List<Object>) result.get("items");
        if (null == items || items.isEmpty()) {
            return null;
        }
        Map<String, Object> last = (Map<String, Object>) items.get(0);
        Object downloadUrl = last.get("downloadUrl");
        if (null == downloadUrl) {
            return null;
        }
        return downloadUrl.toString();
    }

//    public static void main(String[] args) {
//        MavenConfig mavenConfig = new MavenConfig();
//        mavenConfig.setHost("http://127.0.0.1:8081");
//
//        Nexus3RestFetch dependencyFetch = new Nexus3RestFetch(mavenConfig);
//
//        Dependency dependency = new Dependency();
//        dependency.setGroupId("cn.moyada");
//        dependency.setArtifactId("dubbo-test-api");
//        String url = dependencyFetch.getJarUrl(dependency);
//        System.out.println(url);
//    }
}
