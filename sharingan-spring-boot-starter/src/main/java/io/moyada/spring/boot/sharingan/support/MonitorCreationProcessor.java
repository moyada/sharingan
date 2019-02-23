package io.moyada.spring.boot.sharingan.support;

import io.moyada.sharingan.monitor.api.Register;
import io.moyada.spring.boot.sharingan.config.SharinganConfig;
import io.moyada.spring.boot.sharingan.CommonConfiguration;
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
public class MonitorCreationProcessor extends CommonConfiguration implements BeanFactoryPostProcessor, ResourceLoaderAware {

    private String[] basePackages;

    private SharinganConfig sharinganConfig;

    private Register register;

    private ResourceLoader resourceLoader;

    public MonitorCreationProcessor(String[] basePackages, SharinganConfig sharinganConfig, Register register) {
        this.basePackages = basePackages;
        this.sharinganConfig = sharinganConfig;
        this.register = register;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory defaultBeanFactory = (DefaultListableBeanFactory) beanFactory;
            if (!defaultBeanFactory.isAllowBeanDefinitionOverriding()) {
                defaultBeanFactory.setAllowBeanDefinitionOverriding(true);
            }
        }

        MonitorBeanDefinitionScanner scanner = new MonitorBeanDefinitionScanner((BeanDefinitionRegistry) beanFactory,
                sharinganConfig, register);
        scanner.setResourceLoader(this.resourceLoader);
        scanner.scan(basePackages);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
