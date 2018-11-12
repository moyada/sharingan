package cn.moyada.sharingan.spring.boot.autoconfigure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(SharinganConfig.PREFIX_NAME)
public class SharinganConfig implements EnvironmentAware {

    public static final String PREFIX_NAME = "sharingan";

    private boolean enable = false;

    private String application;

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

}
