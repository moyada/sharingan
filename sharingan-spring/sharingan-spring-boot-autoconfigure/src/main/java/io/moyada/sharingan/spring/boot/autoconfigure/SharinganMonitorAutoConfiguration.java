package io.moyada.sharingan.spring.boot.autoconfigure;


import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.Register;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.EnableMonitor;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.util.BeanDefinitionUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 监视逻辑自动配置
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
@ConditionalOnClass(Monitor.class)
@ConditionalOnProperty(name = EnableMonitor.ENABLE_PREFIX, havingValue = "true")
@EnableConfigurationProperties({
        SharinganConfig.class,
        MonitorConfig.class
})
@AutoConfigureAfter(MysqlMonitorConfiguration.class)
public class SharinganMonitorAutoConfiguration extends SharinganMonitorConfiguration implements ApplicationContextAware {

    private String[] basePackages;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<String> backPackages = BeanDefinitionUtil.getBasePackages(applicationContext);
        if (backPackages == null) {
            return;
        }

        this.basePackages = backPackages.toArray(new String[0]);
    }

    @Bean
    @ConditionalOnMissingBean(MonitorProcessor.class)
    public MonitorProcessor monitorProcessor(SharinganConfig sharinganConfig,
                                             Register register) {
        sharinganConfig.setBasePackages(basePackages);
        return new MonitorProcessor(sharinganConfig, register);
    }
}
