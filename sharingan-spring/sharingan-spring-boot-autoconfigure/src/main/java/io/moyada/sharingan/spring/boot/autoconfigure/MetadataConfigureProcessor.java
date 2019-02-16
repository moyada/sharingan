package io.moyada.sharingan.spring.boot.autoconfigure;

import io.moyada.sharingan.monitor.api.Register;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.util.BeanDefinitionUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MetadataConfigureProcessor extends ProcessorSupport implements BeanFactoryPostProcessor, ApplicationContextAware, PriorityOrdered {

    private SharinganConfig sharinganConfig;
    private Register register;

    private ApplicationContext applicationContext;
    private String[] basePackages;

    public MetadataConfigureProcessor(SharinganConfig sharinganConfig, Register register) {
        this.sharinganConfig = sharinganConfig;
        this.register = register;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        List<String> backPackages = BeanDefinitionUtil.getBasePackages(applicationContext);
        if (backPackages == null) {
            sharinganConfig.setEnable(false);
        } else {
            this.basePackages = backPackages.toArray(new String[0]);
        }
    }

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
        scanner.setResourceLoader(this.applicationContext);
        scanner.scan(this.basePackages);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
