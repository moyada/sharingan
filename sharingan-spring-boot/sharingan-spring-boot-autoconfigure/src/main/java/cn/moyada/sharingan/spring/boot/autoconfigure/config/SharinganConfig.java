package cn.moyada.sharingan.spring.boot.autoconfigure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(SharinganConfig.PREFIX_NAME)
@Lazy(false)
public class SharinganConfig implements EnvironmentAware {

    static final String PREFIX_NAME = "sharingan";

    private boolean enable = false;

    private String application;

    private String proxyType;

    @Override
    public void setEnvironment(Environment environment) {
        Boolean enable = environment.getProperty(PREFIX_NAME + ".enable", Boolean.class);
        if (null != enable) {
            this.enable = enable;
        }

        String application = environment.getProperty(PREFIX_NAME + ".application", String.class);
        if (null != application) {
            this.application = application;
        }

        String proxyType = environment.getProperty(PREFIX_NAME + ".proxy-type", String.class);
        if (null != proxyType) {
            this.proxyType = proxyType;
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }
}
