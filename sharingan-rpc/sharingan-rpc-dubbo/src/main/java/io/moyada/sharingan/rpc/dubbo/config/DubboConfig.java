package io.moyada.sharingan.rpc.dubbo.config;


import io.moyada.sharingan.infrastructure.util.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * dubbo配置项
 * @author xueyikang
 * @since 0.0.1
 */
@ConfigurationProperties(DubboConfig.PREFIX)
public class DubboConfig {

    public static final String PREFIX = "sharingan.rpc.dubbo";

    // 注册中心地址
    private String[] registry;

    // 协议
    private String protocol = "dubbo";

    // 注册中心账户
    private String username;

    // 注册中心密码
    private String password;

    // 超时
    private Integer timeout = 3000;

    public String[] getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        if (StringUtil.isEmpty(registry)) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
