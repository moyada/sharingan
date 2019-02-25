package io.moyada.spring.boot.sharingan;


import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.Register;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.spring.boot.sharingan.annotation.EnableSharinganMonitor;
import io.moyada.spring.boot.sharingan.annotation.EnableSharinganMonitorConfig;
import io.moyada.spring.boot.sharingan.config.SharinganConfig;
import io.moyada.spring.boot.sharingan.config.SharinganProperties;
import io.moyada.spring.boot.sharingan.context.ScanPackages;
import io.moyada.spring.boot.sharingan.support.MonitorCreationProcessor;
import io.moyada.spring.boot.sharingan.util.BeanDefinitionUtil;
import io.moyada.spring.boot.sharingan.util.PropertiesUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 监视逻辑自动配置
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConditionalOnClass(Monitor.class)
@ConditionalOnBean(annotation = EnableSharinganMonitorConfig.class)
@ConditionalOnMissingBean(SharinganMonitorAutoConfiguration.class)
@ConditionalOnProperty(name = EnableSharinganMonitor.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({
        SharinganConfig.class,
        MonitorConfig.class
})
@AutoConfigureBefore(MysqlMonitorConfiguration.class)
public class SharinganMonitorAutoConfiguration extends CommonConfiguration implements EnvironmentAware, PriorityOrdered, BeanFactoryAware, ApplicationContextAware {

    private Environment env;

    private String[] basePackages;

    @Bean("sharinganConfig")
    @ConditionalOnMissingBean(value = SharinganConfig.class)
    public SharinganConfig sharinganConfig() {
        SharinganConfig sharinganConfig = new SharinganConfig();
        sharinganConfig.setEnable(env.getProperty(EnableSharinganMonitor.ENABLE_PREFIX, Boolean.class, Boolean.TRUE));
        sharinganConfig.setApplication(env.getProperty(MonitorConfig.PREFIX + ".application"));
        sharinganConfig.setGroupId(env.getProperty(MonitorConfig.PREFIX + ".group-id"));
        sharinganConfig.setArtifactId(env.getProperty(MonitorConfig.PREFIX + ".artifact-id"));
        sharinganConfig.setAttach(PropertiesUtil.getMap(env, SharinganProperties.ATTACH));
        return sharinganConfig;
    }

    @Bean("monitorConfig")
    @ConditionalOnMissingBean(value = MonitorConfig.class)
    public MonitorConfig monitorConfig() {
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setType(env.getProperty(MonitorConfig.PREFIX + ".type"));
        monitorConfig.setRegister(env.getProperty(MonitorConfig.PREFIX + ".register"));
        monitorConfig.setIntervalTime(env.getProperty(MonitorConfig.PREFIX + ".interval-time", Integer.class, -1));
        monitorConfig.setThresholdSize(env.getProperty(MonitorConfig.PREFIX + ".threshold-size", Integer.class, -1));
        return monitorConfig;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        ScanPackages bean;
        try {
            bean = beanFactory.getBean(ScanPackages.class);
        } catch (BeansException e) {
            return;
        }
        this.basePackages = bean.getBasePackages();

        if (beanFactory instanceof DefaultListableBeanFactory) {
            ((DefaultListableBeanFactory) beanFactory).removeBeanDefinition(ScanPackages.BEAN_NAME);
            ((DefaultListableBeanFactory) beanFactory).destroyBean(bean);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.basePackages != null) {
            return;
        }
        List<String> backPackages = BeanDefinitionUtil.getBasePackages(applicationContext);
        if (backPackages == null) {
            return;
        }

        this.basePackages = backPackages.toArray(new String[0]);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(MonitorCreationProcessor.class)
    public MonitorCreationProcessor monitorProcessor(SharinganConfig sharinganConfig, @Nullable Register register) {
        if (!sharinganConfig.isEnable()) {
            return null;
        }
        return new MonitorCreationProcessor(basePackages, sharinganConfig, register);
    }
}
