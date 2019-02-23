package io.moyada.sharingan.spring.boot.starter.support;

import io.moyada.sharingan.monitor.api.Register;
import io.moyada.sharingan.spring.boot.starter.CommonConfiguration;
import io.moyada.sharingan.spring.boot.starter.config.SharinganConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorProcessor extends CommonConfiguration implements BeanFactoryPostProcessor, ResourceLoaderAware {

    private SharinganConfig sharinganConfig;

    private Register register;

    public MonitorProcessor(SharinganConfig sharinganConfig, Register register) {
        this.sharinganConfig = sharinganConfig;
        this.register = register;
    }

    private ResourceLoader resourceLoader;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (!sharinganConfig.isEnable()) {
            return;
        }

        if (beanFactory instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory defaultBeanFactory = (DefaultListableBeanFactory) beanFactory;
            if (!defaultBeanFactory.isAllowBeanDefinitionOverriding()) {
                defaultBeanFactory.setAllowBeanDefinitionOverriding(true);
            }
        }

        MonitorBeanDefinitionScanner scanner = new MonitorBeanDefinitionScanner((BeanDefinitionRegistry) beanFactory,
                sharinganConfig, register);
        scanner.setResourceLoader(this.resourceLoader);
        scanner.scan(sharinganConfig.getBasePackages());
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
