package cn.moyada.sharingan.rpc.dubbo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * dubbo配置项
 * @author xueyikang
 * @since 1.0
 */
//@Component
@ConfigurationProperties(DubboConfig.DUBBO_CONFIG_PREFIX)
public class DubboConfig {

    static final String DUBBO_CONFIG_PREFIX = "dubbo";

    // 注册中心地址
    private String registry;

    // 注册中心账户
    private String username;

    // 注册中心密码
    private String password;

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
