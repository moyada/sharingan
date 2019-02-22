package io.moyada.sharingan.spring.boot.autoconfigure;

import io.moyada.sharingan.monitor.api.Register;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorScannerProcessor extends SharinganMonitorConfiguration implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Bean
    @ConditionalOnMissingBean(MonitorProcessor.class)
    public MonitorProcessor monitorProcessor(SharinganConfig sharinganConfig,
                                             Register register) {
        ScanPackages bean = beanFactory.getBean(ScanPackages.class);
        sharinganConfig.setBasePackages(bean.getBasePackages());
        return new MonitorProcessor(sharinganConfig, register);
    }
}
