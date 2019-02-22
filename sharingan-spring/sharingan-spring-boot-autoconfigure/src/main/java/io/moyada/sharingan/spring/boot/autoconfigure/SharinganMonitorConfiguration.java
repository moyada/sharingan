package io.moyada.sharingan.spring.boot.autoconfigure;


import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.EnableMonitor;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganProperties;
import io.moyada.sharingan.spring.boot.autoconfigure.util.PropertiesUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;

/**
 * 监视逻辑自动配置
 * @author xueyikang
 * @since 1.0
 **/
class SharinganMonitorConfiguration extends ProcessorSupport implements EnvironmentAware, PriorityOrdered {

    private Environment env;

    @Bean("sharinganConfig")
    @ConditionalOnMissingBean(value = SharinganConfig.class)
    public SharinganConfig sharinganConfig() {
        SharinganConfig sharinganConfig = new SharinganConfig();
        sharinganConfig.setEnable(env.getProperty(EnableMonitor.ENABLE_PREFIX, Boolean.class, Boolean.TRUE));
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
        monitorConfig.setIntervalTime(env.getProperty(MonitorConfig.PREFIX + ".interval-time", Integer.class, -1));
        monitorConfig.setThresholdSize(env.getProperty(MonitorConfig.PREFIX + ".threshold-size", Integer.class, -1));
        return monitorConfig;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
