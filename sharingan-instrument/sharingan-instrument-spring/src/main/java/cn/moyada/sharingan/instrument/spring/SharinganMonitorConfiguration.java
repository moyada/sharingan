package cn.moyada.sharingan.instrument.spring;

import cn.moyada.sharingan.instrument.spring.config.SharinganConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
public class SharinganMonitorConfiguration {

    @Bean
    public SharinganConfig sharinganConfig() {
        return new SharinganConfig();
    }

    @Bean
    public MonitorAnnotationBeanPostProcessor monitorProcessor() {
        return new MonitorAnnotationBeanPostProcessor();
    }
}
