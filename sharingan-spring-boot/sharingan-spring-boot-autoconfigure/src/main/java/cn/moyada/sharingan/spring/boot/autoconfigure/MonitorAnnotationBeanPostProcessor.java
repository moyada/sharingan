package cn.moyada.sharingan.spring.boot.autoconfigure;

import cn.moyada.sharingan.instrument.boost.ClassUtil;
import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistInheritProxy;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistProxy;
import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.api.annotation.Exclusive;
import cn.moyada.sharingan.monitor.api.entity.DefaultInvocation;
import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.spring.boot.autoconfigure.config.SharinganProperties;
import cn.moyada.sharingan.spring.boot.autoconfigure.util.PropertiesUtil;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.*;
import org.springframework.lang.NonNull;

import java.util.*;

/**
 * 监视器代理解析器
 */
public class MonitorAnnotationBeanPostProcessor implements InitializingBean, EnvironmentAware, BeanFactoryPostProcessor, PriorityOrdered {

    private boolean enable = false;

    private Map<String, Object> attach;

    private JavassistProxy<Invocation> javassistProxy;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!enable) {
            return;
        }

        try {
            javassistProxy = new JavassistInheritProxy<>(Monitor.class,
                    Monitor.class.getDeclaredMethod("listener", Invocation.class),
                    Invocation.class, DefaultInvocation.class, attach, Variables.INJECT_APP_NAME);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanDefinitionRegistry) throws BeansException {
        if (!enable || javassistProxy == null) {
            return;
        }

        String[] beanDefinitionNames = beanDefinitionRegistry.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);

            if (beanDefinition instanceof AbstractBeanDefinition) {

                AbstractBeanDefinition definition = (AbstractBeanDefinition) beanDefinition;
                Class<?> beanClass = getClass(definition);

                if (null == beanClass) {
                    continue;
                }

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

                // 修改 BeanDefinition 类信息
                definition.setBeanClass(beanClass);
            }
        }
    }

    /**
     * 获取类信息
     * @param definition
     * @return
     */
    private Class getClass(AbstractBeanDefinition definition) {
        Class<?> beanClass;
        if (definition.hasBeanClass()) {
            beanClass = definition.getBeanClass();
        } else {
            String beanClassName = definition.getBeanClassName();
            if (null == beanClassName) {
                return null;
            }
            try {
                beanClass = Class.forName(beanClassName);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return beanClass;
    }

    @Override
    public void setEnvironment(Environment environment) {
        Boolean property = environment.getProperty(SharinganProperties.ENABLE, boolean.class);
        if (null != property) {
            this.enable = property;
        }

        attach = PropertiesUtil.getMap(environment, SharinganProperties.ATTACH);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
