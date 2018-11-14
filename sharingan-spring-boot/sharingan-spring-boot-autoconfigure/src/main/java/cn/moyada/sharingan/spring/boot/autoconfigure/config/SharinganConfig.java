package cn.moyada.sharingan.spring.boot.autoconfigure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 监视器配置
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(prefix = SharinganProperties.PREFIX_NAME)
public class SharinganConfig {

    /**
     * 启用
     */
    private boolean enable = false;

    /**
     * 项目名
     */
    private String application;

    /**
     * 当前环境属性
     */
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

    public Map<String, String> getAttach() {
        return attach;
    }

    public void setAttach(Map<String, String> attach) {
        this.attach = attach;
    }
}
