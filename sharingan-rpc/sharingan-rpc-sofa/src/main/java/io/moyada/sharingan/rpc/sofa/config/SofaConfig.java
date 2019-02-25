package io.moyada.sharingan.rpc.sofa.config;

import io.moyada.sharingan.infrastructure.util.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Component
@ConfigurationProperties(SofaConfig.PREFIX)
public class SofaConfig {

    public static final String PREFIX = "sharingan.rpc.sofa";

    // 注册中心
    private String[] registry;

    // 协议
    private String protocol = "bolt";

    // 直连地址
    private String directUrl;

    private int timeout = 3000;

    public String[] getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        if (null == registry) {
            return;
        }
        this.registry = registry.split(",");
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        if (StringUtil.isEmpty(protocol)) {
            return;
        }
        this.protocol = protocol;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        if (timeout == null) {
            return;
        }
        this.timeout = timeout;
    }
}
