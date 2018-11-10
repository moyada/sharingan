package cn.moyada.sharingan.monitor.local;

import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.api.TestMonitor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
public class LocalMonitorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Monitor monitor() {
        return new TestMonitor();
    }
}
