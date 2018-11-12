package cn.moyada.sharingan.spring.boot.autoconfigure;


import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.api.TestMonitor;
import cn.moyada.sharingan.monitor.api.annotation.Listener;
import cn.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
@ConditionalOnProperty(value = SharinganConfig.PREFIX_NAME + ".enable", havingValue = "true")
@ConditionalOnClass(Listener.class)
@EnableConfigurationProperties(SharinganConfig.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class SharinganMonitorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MonitorAnnotationBeanPostProcessor monitorProcessor() {
        return new MonitorAnnotationBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public Monitor monitor() {
        return new TestMonitor();
    }
}
