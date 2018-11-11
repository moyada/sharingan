package cn.moyada.sharingan.spring.boot.autoconfigure;


import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.api.TestMonitor;
import cn.moyada.sharingan.monitor.api.annotation.Listener;
import cn.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
@ConditionalOnClass(Listener.class)
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@EnableConfigurationProperties(SharinganConfig.class)
public class SharinganMonitorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SharinganConfig.class)
    public SharinganConfig sharinganConfig() {
        return new SharinganConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public Monitor monitor() {
        return new TestMonitor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MonitorAnnotationBeanPostProcessor monitorProcessor() {
        return new MonitorAnnotationBeanPostProcessor();
    }
}
