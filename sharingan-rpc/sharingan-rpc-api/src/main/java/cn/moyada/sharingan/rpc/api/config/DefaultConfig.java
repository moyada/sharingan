package cn.moyada.sharingan.rpc.api.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 基础配置项
 * @author xueyikang
 * @since 0.0.1
 */
@Component
@ConfigurationProperties(DefaultConfig.DEFAULT_CONFIG_PREFIX)
public class DefaultConfig {

    static final String DEFAULT_CONFIG_PREFIX = "";

    /**
     * 项目名
     */
    @Value("${project.name}")
    private String identifyName;

    public String getIdentifyName() {
        return identifyName;
    }

    public void setIdentifyName(String identifyName) {
        this.identifyName = identifyName;
    }
}
