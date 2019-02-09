package io.moyada.sharingan.rpc.dubbo.config;


import io.moyada.sharingan.infrastructure.util.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * dubbo配置项
 * @author xueyikang
 * @since 0.0.1
 */
@Component
@ConfigurationProperties("sharingan.rpc.dubbo")
public class DubboConfig {

    // 注册中心地址
    private String registry;

    // 协议
    private String protocol;

    // 注册中心账户
    private String username;

    // 注册中心密码
    private String password;

    // 超时
    private Integer timeout;

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getProtocol() {
        return StringUtil.isEmpty(protocol) ? "dubbo" : protocol;
    }

    public void setProtocol(String protocol) {
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

    public Integer getTimeout() {
        return null == timeout ? 3000 : timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
