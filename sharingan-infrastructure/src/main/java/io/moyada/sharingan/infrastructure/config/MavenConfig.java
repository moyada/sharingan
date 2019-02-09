package io.moyada.sharingan.infrastructure.config;


import io.moyada.sharingan.infrastructure.util.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * maven仓库配置
 * @author xueyikang
 * @since 0.0.1
 **/
@Order(-1)
@Component("mavenConfig")
@ConfigurationProperties("sharingan.maven")
public class MavenConfig {

    /**
     * 协议+域名+端口
     */
    private String registry;

    /**
     * 账户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public boolean isCredential() {
       return !StringUtil.isEmpty(username) && !StringUtil.isEmpty(password);
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
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
}
