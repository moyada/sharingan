package cn.moyada.sharingan.spring.boot.autoconfigure;

import cn.moyada.sharingan.instrument.boost.ClassUtil;
import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistInheritProxy;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistProxy;
import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.api.annotation.Exclusive;
import cn.moyada.sharingan.monitor.api.entity.DefaultInvocation;
import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.NonNull;

import java.util.List;

public class MonitorAnnotationBeanPostProcessor implements InitializingBean, BeanFactoryAware, BeanFactoryPostProcessor, PriorityOrdered {

//    private Set<String> proxyBeanNames = new HashSet<>();

//    private Monitor monitorBean;

    private SharinganConfig sharinganConfig;

    private JavassistProxy<Invocation> javassistProxy;

//    private BeanDefinitionRegistry beanDefinitionRegistry;

//    private BeanCopier.Generator generator;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!sharinganConfig.isEnable()) {
            return;
        }

        try {
            javassistProxy = new JavassistInheritProxy<>(Monitor.class,
                    Monitor.class.getDeclaredMethod("listener", Invocation.class),
                    Invocation.class, DefaultInvocation.class, Variables.APP_INFO);
        } catch (NoSuchMethodException e) {
            sharinganConfig.setEnable(false);
        }

//        generator = new BeanCopier.Generator();
    }

//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        this.beanDefinitionRegistry = registry;
//    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanDefinitionRegistry) throws BeansException {
        if (!sharinganConfig.isEnable() || javassistProxy == null) {
            return;
        }

        String[] beanDefinitionNames = beanDefinitionRegistry.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            if (beanDefinition instanceof AbstractBeanDefinition) {
                AbstractBeanDefinition definition = (AbstractBeanDefinition) beanDefinition;
                Class<?> beanClass;

                if (definition.hasBeanClass()) {
                    beanClass = definition.getBeanClass();
                } else {
                    String beanClassName = definition.getBeanClassName();
                    if (null == beanClassName) {
                        continue;
                    }
                    try {
                        beanClass = Class.forName(beanClassName);
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                        continue;
                    }
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

                definition.setBeanClass(beanClass);
            }
        }
    }

//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if (sharinganConfig.isEnable() && javassistProxy != null && proxyBeanNames.contains(beanName)) {
//            try {
//                injectProperty(bean);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//            } finally {
//                proxyBeanNames.remove(beanName);
//            }
//        }
//        return bean;
//    }

//    private void injectProperty(Object bean) throws NoSuchFieldException, IllegalAccessException {
//        Class<?> clazz = bean.getClass();
//
//        Field appField = clazz.getDeclaredField(Variables.APP_PARAM);
//        appField.setAccessible(true);
//        appField.set(bean, sharinganConfig.getApplication());
//
//        Field monitorField = clazz.getDeclaredField(Variables.MONITOR_PARAM);
//        monitorField.setAccessible(true);
//        monitorField.set(bean, monitorBean);
//    }

//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (!sharinganConfig.isEnable() || javassistProxy == null) {
//            return bean;
//        }
//
//        Class<?> target = bean.getClass();
//        List<ProxyMethod> proxyMethods = ClassUtil.getProxyInfo(bean.getClass(), Exclusive.class);
//        if (null == proxyMethods) {
//            return bean;
//        }
//
//        Class<?> proxy;
//        try {
//            proxy = javassistProxy.wrapper(target, proxyMethods);
//        } catch (NotFoundException | CannotCompileException e) {
//            e.printStackTrace();
//            return bean;
//        }
//
//        BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
//        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
//        int argumentCount = constructorArgumentValues.getArgumentCount();
//
//        Object instance;
//        if (0 == argumentCount) {
//            try {
//                instance = proxy.getDeclaredConstructor().newInstance();
//            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//                return bean;
//            }
//        } else {
//            List<ConstructorArgumentValues.ValueHolder> values = constructorArgumentValues.getGenericArgumentValues();
//            Class<?>[] argsTypes = new Class[argumentCount];
//            Object[] argsValues = new Object[argumentCount];
//            for (int i = 0; i < argumentCount; i++) {
//                ConstructorArgumentValues.ValueHolder valueHolder = values.get(i);
//                argsValues[i] = valueHolder.getValue();
//                try {
//                    argsTypes[i] = Class.forName(valueHolder.getType());
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                    return bean;
//                }
//            }
//
//            try {
//                instance = proxy.getDeclaredConstructor(argsTypes).newInstance(argsValues);
//            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//                return bean;
//            }
//        }
//
//        generator.setSource(target);
//        generator.setTarget(proxy);
//        BeanCopier beanCopier = generator.create();
//        beanCopier.copy(bean, instance, null);
//
//        try {
//            injectProperty(instance);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            return bean;
//        }
//
//        return instance;
//    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        try {
            sharinganConfig = beanFactory.getBean(SharinganConfig.class);
        } catch (BeansException e) {
            sharinganConfig = new SharinganConfig();
        }

        try {
            beanFactory.getBean(Monitor.class);
        } catch (BeansException e) {
            sharinganConfig.setEnable(false);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
