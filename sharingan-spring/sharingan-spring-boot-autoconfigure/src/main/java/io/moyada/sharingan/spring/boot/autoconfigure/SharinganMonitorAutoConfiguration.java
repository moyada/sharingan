package io.moyada.sharingan.spring.boot.autoconfigure;


import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.database.DataBaseMonitor;
import io.moyada.sharingan.monitor.database.config.ApplicationConfig;
import io.moyada.sharingan.monitor.database.config.DataConfig;
import io.moyada.sharingan.monitor.database.config.DataSourceConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganProperties;
import io.moyada.sharingan.spring.boot.autoconfigure.util.PropertiesUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 监视逻辑自动配置
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
@ConditionalOnClass(Monitor.class)
@ConditionalOnProperty(name = MonitorConfig.PREFIX + ".enable", havingValue = "true")
@EnableConfigurationProperties({
        SharinganConfig.class,
        MonitorConfig.class,
        DataSourceConfig.class,
        ApplicationConfig.class,
        DataConfig.class
})
public class SharinganMonitorAutoConfiguration implements ApplicationContextAware {

    private ConfigurableEnvironment env;

    @Bean
    @ConditionalOnMissingBean(value = SharinganConfig.class)
    public SharinganConfig sharinganConfig() {
        SharinganConfig sharinganConfig = new SharinganConfig();
        sharinganConfig.setApplication(env.getProperty(MonitorConfig.PREFIX + ".application"));
        sharinganConfig.setType(env.getProperty(MonitorConfig.PREFIX + ".type"));
        sharinganConfig.setAttach(PropertiesUtil.getMap(env, SharinganProperties.ATTACH));
        return sharinganConfig;
    }

    @Bean
    @ConditionalOnMissingBean(value = MonitorConfig.class)
    public MonitorConfig monitorConfig() {
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setIntervalTime(env.getProperty(MonitorConfig.PREFIX + ".interval-time", Integer.class, -1));
        monitorConfig.setThresholdSize(env.getProperty(MonitorConfig.PREFIX + ".threshold-size", Integer.class, -1));
        return monitorConfig;
    }

    @Bean
    @ConditionalOnMissingBean(value = DataSourceConfig.class)
    public DataSourceConfig dataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverClassName(env.getProperty(MonitorConfig.PREFIX + ".data-source.driver-class-name"));
        dataSourceConfig.setUrl(env.getProperty(MonitorConfig.PREFIX + ".data-source.url"));
        dataSourceConfig.setUsername(env.getProperty(MonitorConfig.PREFIX + ".data-source.username"));
        dataSourceConfig.setPassword(env.getProperty(MonitorConfig.PREFIX + ".data-source.password"));
        return dataSourceConfig;
    }

    @Bean
    @ConditionalOnMissingBean(value = ApplicationConfig.class)
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setTable(env.getProperty(MonitorConfig.PREFIX + ".data-source.app-info.table"));
        applicationConfig.setParamColumn(env.getProperty(MonitorConfig.PREFIX + ".data-source.app-info.param-column"));
        applicationConfig.setResultColumn(env.getProperty(MonitorConfig.PREFIX + ".data-source.app-info.result-column"));
        return applicationConfig;
    }

    @Bean
    @ConditionalOnMissingBean(value = MonitorConfig.class)
    public DataConfig dataConfig() {
        DataConfig dataConfig = new DataConfig();
        dataConfig.setTable(env.getProperty(MonitorConfig.PREFIX + ".data-source.store-info.table"));
        dataConfig.setColumn(env.getProperty(MonitorConfig.PREFIX + ".data-source.store-info.column"));
        return dataConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        env = applicationContext.getBean(ConfigurableEnvironment.class);
    }

    @Configuration
    protected static class RefreshableConfiguration {

        @Bean
        @ConditionalOnMissingBean(Monitor.class)
        public Monitor monitor(MonitorConfig monitorConfig,
                               SharinganConfig sharinganConfig,
                               DataSourceConfig dataSourceConfig,
                               ApplicationConfig applicationConfig,
                               DataConfig dataConfig) {
            if (null == monitorConfig) {
                monitorConfig = new MonitorConfig();
            }
            String type = sharinganConfig.getType();

            switch (type) {
                case "Database":
                    return createDataBase(monitorConfig, dataSourceConfig, applicationConfig, dataConfig);
            }

            return null;
        }

        private Monitor createDataBase(MonitorConfig monitorConfig,
                                       DataSourceConfig dataSourceConfig,
                                       ApplicationConfig applicationConfig,
                                       DataConfig dataConfig) {
            return DataBaseMonitor.newInstance(monitorConfig, dataSourceConfig, applicationConfig, dataConfig);
        }

        @Bean
        @ConditionalOnMissingBean(MonitorBeanScannerConfigurer.class)
        public MonitorBeanScannerConfigurer monitorProcessor(SharinganConfig sharinganConfig) {
            return new MonitorBeanScannerConfigurer(sharinganConfig);
        }
    }
}
