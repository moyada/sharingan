package cn.moyada.sharingan.module;

import cn.moyada.sharingan.common.utils.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Order(-1)
@Component("mavenConfig")
@ConfigurationProperties(prefix = "maven")
public class MavenConfig {

    private String host;

    private String username;

    private String password;

    public boolean isCredential() {
       return !StringUtil.isEmpty(username) && !StringUtil.isEmpty(password);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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
