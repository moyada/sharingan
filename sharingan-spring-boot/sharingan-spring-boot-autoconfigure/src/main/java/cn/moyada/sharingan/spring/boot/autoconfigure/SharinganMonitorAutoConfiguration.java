package cn.moyada.sharingan.spring.boot.autoconfigure;


import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.mysql.MysqlConfig;
import cn.moyada.sharingan.monitor.mysql.MysqlMonitorFactory;
import cn.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import cn.moyada.sharingan.spring.boot.autoconfigure.config.SharinganProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 监视逻辑自动配置
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
@ConditionalOnClass(Monitor.class)
@ConditionalOnProperty(name = SharinganProperties.ENABLE, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({SharinganConfig.class, MysqlConfig.class})
public class SharinganMonitorAutoConfiguration {

    @Autowired
    private SharinganConfig sharinganConfig;

    @Autowired
    private MysqlConfig mysqlConfig;

    @Bean
    @ConditionalOnMissingBean
    public Monitor monitor() {
        if (!sharinganConfig.isEnable()) {
            return null;
        }
//        return new TestMonitor();
         return MysqlMonitorFactory.build(mysqlConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public static MonitorBeanScannerConfigurer monitorProcessor() {
        return new MonitorBeanScannerConfigurer();
    }
}
