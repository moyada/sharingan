package io.moyada.sharingan.spring.boot.autoconfigure;

import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.util.BeanDefinitionUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorBeanScannerConfigurer extends AbstractConfigureProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private String[] basePackages;

    public MonitorBeanScannerConfigurer(SharinganConfig sharinganConfig) {
        super(sharinganConfig);
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
                sharinganConfig.getApplication(), sharinganConfig.getAttach());
        scanner.setResourceLoader(this.applicationContext);
        scanner.scan(this.basePackages);
    }
}
