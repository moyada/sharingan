package cn.moyada.sharingan.rpc.springcloud;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Arrays;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class FeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (containsBeanDefinition(beanFactory, "feignContext", "eurekaAutoServiceRegistration")) {
            BeanDefinition bd = beanFactory.getBeanDefinition("feignContext");
            bd.setDependsOn("eurekaAutoServiceRegistration");
        }
    }

    private static boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String... beans) {
        return Arrays.stream(beans).allMatch(beanFactory::containsBeanDefinition);
    }
}