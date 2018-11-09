package cn.moyada.sharingan.instrument.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties("sharingan")
@Lazy(false)
public class SharinganConfig implements EnvironmentAware {

    private boolean enable = false;

    private String application;

    private String address;

    @Override
    public void setEnvironment(Environment environment) {
        Boolean enable = environment.getProperty("sharingan.enable", Boolean.class);
        if (null != enable) {
            this.enable = enable;
        }

        String application = environment.getProperty("sharingan.application", String.class);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
