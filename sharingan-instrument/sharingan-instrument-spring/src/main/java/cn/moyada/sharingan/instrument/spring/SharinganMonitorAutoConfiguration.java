package cn.moyada.sharingan.instrument.spring;

import cn.moyada.sharingan.instrument.spring.config.SharinganConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
public class SharinganMonitorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = SharinganConfig.class)
    public SharinganConfig sharinganConfig() {
        return new SharinganConfig();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public Monitor monitor() {
//        return new TestMonitor();
//    }

    @Bean
    @ConditionalOnMissingBean
    public MonitorAnnotationBeanPostProcessor monitorProcessor() {
        return new MonitorAnnotationBeanPostProcessor();
    }
}
