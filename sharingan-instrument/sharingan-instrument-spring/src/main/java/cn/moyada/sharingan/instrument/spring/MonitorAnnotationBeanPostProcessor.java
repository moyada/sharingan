package cn.moyada.sharingan.instrument.spring;

import cn.moyada.sharingan.instrument.boost.ClassUtil;
import cn.moyada.sharingan.instrument.boost.JavassistProxy;
import cn.moyada.sharingan.instrument.boost.ProxyMethod;
import cn.moyada.sharingan.instrument.spring.config.SharinganConfig;
import cn.moyada.sharingan.monitor.api.DefaultInvocation;
import cn.moyada.sharingan.monitor.api.Exclusive;
import cn.moyada.sharingan.monitor.api.Invocation;
import cn.moyada.sharingan.monitor.api.Monitor;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MonitorAnnotationBeanPostProcessor implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor, BeanFactoryAware, Ordered {

    private Set<String> beanNames = new HashSet<>();

    private Monitor monitorBean;

    @Autowired
    private SharinganConfig sharinganConfig;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        if (!sharinganConfig.isEnable()) {
            return;
        }

        String[] beanDefinitionNames = beanDefinitionRegistry.getBeanDefinitionNames();

        JavassistProxy<Invocation> javassistProxy;
        try {
            javassistProxy = new JavassistProxy<>(Monitor.class,
                    Monitor.class.getDeclaredMethod("listener", Invocation.class),
                    Invocation.class, DefaultInvocation.class, Variables.APP_INFO);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }

        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            if (beanDefinition instanceof AbstractBeanDefinition) {
                AbstractBeanDefinition definition = (AbstractBeanDefinition) beanDefinition;
                Class<?> beanClass = definition.getBeanClass();

                List<ProxyMethod> proxyInfo = ClassUtil.getProxyInfo(beanClass, Exclusive.class);

                if (null == proxyInfo) {
                    continue;
                }

                try {
                    beanClass = javassistProxy.wrapper(beanClass, proxyInfo);
                } catch (NotFoundException | CannotCompileException e) {
                    e.printStackTrace();
                    continue;
                }

                definition.setBeanClass(beanClass);
                beanNames.add(beanDefinitionName);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (sharinganConfig.isEnable() && beanNames.contains(beanName)) {
            try {
                injectProperty(bean);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    private void injectProperty(Object bean) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = bean.getClass();

        Field appField = clazz.getDeclaredField(Variables.APP_PARAM);
        appField.setAccessible(true);
        appField.set(bean, sharinganConfig.getApplication());

        Field monitorField = clazz.getDeclaredField(Variables.MONITOR_PARAM);
        monitorField.setAccessible(true);
        monitorField.set(bean, monitorBean);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        monitorBean = beanFactory.getBean(Monitor.class);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
