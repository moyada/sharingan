package io.moyada.spring.boot.sharingan;


import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.Register;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.mysql.MysqlMonitor;
import io.moyada.sharingan.monitor.mysql.MysqlRegister;
import io.moyada.sharingan.monitor.mysql.config.*;
import io.moyada.spring.boot.sharingan.annotation.EnableSharinganMonitor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;

/**
 * 监视逻辑自动配置
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConditionalOnBean(MonitorConfig.class)
@ConditionalOnProperty(name = EnableSharinganMonitor.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({
        DataSourceConfig.class,
        AppConfig.class,
        ServiceConfig.class,
        HttpConfig.class,
        FunctionConfig.class,
        DataMapperConfig.class
})
public class MysqlMonitorConfiguration implements EnvironmentAware {

    private Environment env;

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
    @ConditionalOnMissingBean(value = AppConfig.class)
    public AppConfig appConfig() {
        AppConfig appConfig = new AppConfig();
        appConfig.setTable(env.getProperty(MonitorConfig.PREFIX + ".app-mapper.table"));
        appConfig.setKeyColumn(env.getProperty(MonitorConfig.PREFIX + ".app-mapper.key-column"));
        appConfig.setNameColumn(env.getProperty(MonitorConfig.PREFIX + ".app-mapper.name-column"));
        return appConfig;
    }

    @Bean
    @ConditionalOnMissingBean(value = ServiceConfig.class)
    public ServiceConfig serviceConfig() {
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setTable(env.getProperty(MonitorConfig.PREFIX + ".service-mapper.table"));
        serviceConfig.setKeyColumn(env.getProperty(MonitorConfig.PREFIX + ".service-mapper.key-column"));
        serviceConfig.setNameColumn(env.getProperty(MonitorConfig.PREFIX + ".service-mapper.name-column"));
        return serviceConfig;
    }

    @Bean
    @ConditionalOnMissingBean(value = FunctionConfig.class)
    public FunctionConfig functionConfig() {
        FunctionConfig functionConfig = new FunctionConfig();
        functionConfig.setTable(env.getProperty(MonitorConfig.PREFIX + ".function-mapper.table"));
        functionConfig.setKeyColumn(env.getProperty(MonitorConfig.PREFIX + ".function-mapper.key-column"));
        functionConfig.setNameColumn(env.getProperty(MonitorConfig.PREFIX + ".function-mapper.name-column"));
        return functionConfig;
    }

    @Bean
    @ConditionalOnMissingBean(value = HttpConfig.class)
    public HttpConfig httpConfig() {
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setTable(env.getProperty(MonitorConfig.PREFIX + ".http-mapper.table"));
        httpConfig.setKeyColumn(env.getProperty(MonitorConfig.PREFIX + ".http-mapper.key-column"));
        httpConfig.setNameColumn(env.getProperty(MonitorConfig.PREFIX + ".http-mapper.name-column"));
        return httpConfig;
    }

    @Bean
    @ConditionalOnMissingBean(value = DataMapperConfig.class)
    public DataMapperConfig dataMapperConfig() {
        DataMapperConfig dataMapperConfig = new DataMapperConfig();
        dataMapperConfig.setTable(env.getProperty(MonitorConfig.PREFIX + ".data-mapper.table"));
        dataMapperConfig.setAppColumn(env.getProperty(MonitorConfig.PREFIX + ".data-mapper.app-column"));
        dataMapperConfig.setDomainColumn(env.getProperty(MonitorConfig.PREFIX + ".data-mapper.domain-column"));
        dataMapperConfig.setValueColumn(env.getProperty(MonitorConfig.PREFIX + ".data-mapper.name-column"));
        dataMapperConfig.setDateColumn(env.getProperty(MonitorConfig.PREFIX + ".data-mapper.date-column"));
        return dataMapperConfig;
    }

    @Configuration
    protected static class ModuleConfigure {

        @Bean
        @ConditionalOnProperty(name = MonitorConfig.PREFIX + ".type", havingValue = "mysql")
        @ConditionalOnMissingBean(Monitor.class)
        public Monitor monitor(MonitorConfig monitorConfig,
                               AppConfig appConfig, ServiceConfig serviceConfig,
                               FunctionConfig functionConfig, HttpConfig httpConfig,
                               DataSourceConfig dataSourceConfig, DataMapperConfig dataMapperConfig) {
            MetadataConfig metadataConfig = new MetadataConfig(appConfig, serviceConfig, functionConfig, httpConfig);
            return MysqlMonitor.newInstance(monitorConfig, dataSourceConfig, metadataConfig, dataMapperConfig);
        }

        @Bean
        @ConditionalOnProperty(name = MonitorConfig.PREFIX + ".register", havingValue = "mysql")
        @ConditionalOnMissingBean(Register.class)
        public Register register(AppConfig appConfig, ServiceConfig serviceConfig,
                                 FunctionConfig functionConfig, HttpConfig httpConfig,
                                 DataSourceConfig dataSourceConfig) {
            MetadataConfig metadataConfig = new MetadataConfig(appConfig, serviceConfig, functionConfig, httpConfig);
            return new MysqlRegister(dataSourceConfig, metadataConfig);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}



