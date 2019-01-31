package cn.moyada.sharingan.spring.boot.autoconfigure;

import cn.moyada.sharingan.instrument.boost.ClassUtil;
import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistInheritProxy;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistProxy;
import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.api.annotation.Exclusive;
import cn.moyada.sharingan.monitor.api.entity.DefaultInvocation;
import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.spring.boot.autoconfigure.util.BeanDefinitionUtil;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import java.util.List;

/**
 * 监视器代理解析器
 */
public class MonitorAnnotationBeanPostProcessor extends AbstractConfigureProcessor implements InitializingBean, BeanFactoryPostProcessor {

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
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanDefinitionRegistry) throws BeansException {
        if (!enable || javassistProxy == null) {
            return;
        }

        String[] beanDefinitionNames = beanDefinitionRegistry.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);

            if (beanDefinition instanceof AbstractBeanDefinition) {

                AbstractBeanDefinition definition = (AbstractBeanDefinition) beanDefinition;
                Class<?> beanClass = BeanDefinitionUtil.getClass(definition);

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
}
