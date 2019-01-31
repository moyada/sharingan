package io.moyada.sharingan.infrastructure.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 基础配置项
 * @author xueyikang
 * @since 0.0.1
 */
@Component
public class DefaultConfig {

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
