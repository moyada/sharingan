package io.moyada.sharingan.spring.boot.autoconfigure.config;

import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Component
@ConfigurationProperties(prefix = MonitorConfig.PREFIX)
public class SharinganConfig {

    // 启用
    private boolean enable = true;

    // 应用
    private String application;

    private String type = "Database";

    private Map<String, String> attach;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttach() {
        return attach;
    }

    public void setAttach(Map<String, String> attach) {
        this.attach = attach;
    }
}
