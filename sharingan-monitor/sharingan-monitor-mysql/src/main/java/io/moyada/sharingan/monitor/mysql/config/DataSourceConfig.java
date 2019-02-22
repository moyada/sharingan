package io.moyada.sharingan.monitor.mysql.config;

import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据库连接信息
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(prefix = MonitorConfig.PREFIX + ".data-source")
public class DataSourceConfig {

    /**
     * 驱动
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 地址
     */
    private String url;

    /**
     * 账户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public void checkParam() {
        if (null == driverClassName) {
            throw new NullPointerException("driverClassName is null");
        }
        if (null == url) {
            throw new NullPointerException("url is null");
        }
        if (null == username) {
            throw new NullPointerException("username is null");
        }
        if (null == password) {
            throw new NullPointerException("password is null");
        }
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setDriverClassName(String driverClassName) {
        if (null == driverClassName) {
            return;
        }
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
