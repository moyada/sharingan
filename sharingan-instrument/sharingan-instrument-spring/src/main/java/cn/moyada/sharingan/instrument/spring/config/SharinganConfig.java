package cn.moyada.sharingan.instrument.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties("sharingan")
public class SharinganConfig {

    private boolean enable = false;

    private String application;

    private String address;

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
