package io.moyada.sharingan.spring.boot.autoconfigure;


import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.Register;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.mysql.MysqlMonitor;
import io.moyada.sharingan.monitor.mysql.MysqlRegister;
import io.moyada.sharingan.monitor.mysql.config.*;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganProperties;
import io.moyada.sharingan.spring.boot.autoconfigure.util.PropertiesUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
        AppConfig.class,
        ServiceConfig.class,
        HttpConfig.class,
        FunctionConfig.class,
        DataMapperConfig.class
})
public class SharinganMonitorAutoConfiguration extends ProcessorSupport implements EnvironmentAware {

    private Environment env;

    @Bean
    @ConditionalOnMissingBean(value = SharinganConfig.class)
    public SharinganConfig sharinganConfig() {
        SharinganConfig sharinganConfig = new SharinganConfig();
        sharinganConfig.setApplication(env.getProperty(MonitorConfig.PREFIX + ".application"));
        sharinganConfig.setGroupId(env.getProperty(MonitorConfig.PREFIX + ".group-id"));
        sharinganConfig.setArtifactId(env.getProperty(MonitorConfig.PREFIX + ".artifact-id"));
        sharinganConfig.setMonitorType(env.getProperty(MonitorConfig.PREFIX + ".type"));
        sharinganConfig.setRegisterType(env.getProperty(MonitorConfig.PREFIX + ".register"));
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

    @Bean
    @ConditionalOnMissingBean(Monitor.class)
    public Monitor monitor(MonitorConfig monitorConfig,
                           SharinganConfig sharinganConfig) {
        if (null == monitorConfig) {
            monitorConfig = new MonitorConfig();
        }
        String type = sharinganConfig.getMonitorType();

        switch (type) {
            case Monitor.TYPE:
                return null;
            case MysqlMonitor.TYPE:
                return newMysqlMonitor(monitorConfig);
        }

        return null;
    }

    private Monitor newMysqlMonitor(MonitorConfig monitorConfig) {
        MetadataConfig metadataConfig = new MetadataConfig(appConfig(), serviceConfig(), functionConfig(), httpConfig());
        return MysqlMonitor.newInstance(monitorConfig, dataSourceConfig(), metadataConfig, dataMapperConfig());
    }

    @Bean
    @ConditionalOnMissingBean(Register.class)
    public Register register(SharinganConfig sharinganConfig) {
        String type = sharinganConfig.getRegisterType();
        switch (type) {
            case Register.TYPE:
                return null;
            case MysqlRegister.TYPE:
                return newMysqlRegister();
        }

        return null;
    }

    private Register newMysqlRegister() {
        MetadataConfig metadataConfig = new MetadataConfig(appConfig(), serviceConfig(), functionConfig(), httpConfig());
        return new MysqlRegister(dataSourceConfig(), metadataConfig);
    }

    @Bean
    @ConditionalOnMissingBean(MetadataConfigureProcessor.class)
    public MetadataConfigureProcessor monitorProcessor(SharinganConfig sharinganConfig, Register register) {
        return new MetadataConfigureProcessor(sharinganConfig, register);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
