package io.moyada.sharingan.infrastructure;


import io.moyada.sharingan.infrastructure.exception.InitializeInvokerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 协议代理器
 * @author xueyikang
 * @since 0.0.1
 */
@Component
public class ContextFactory {

    private ApplicationContext applicationContext;

    private DefaultListableBeanFactory beanFactory;

    @Autowired
    public ContextFactory(ApplicationContext applicationContext, DefaultListableBeanFactory beanFactory) {
        this.applicationContext = applicationContext;
        this.beanFactory = beanFactory;
    }

    public void destroyBean(String beanName) {
        Object bean;
        try {
            bean = beanFactory.getBean(beanName);
        } catch (BeansException e) {
            return;
        }
        beanFactory.removeBeanDefinition(beanName);
        beanFactory.destroyBean(bean);
    }

    public <T> T getBean(Class<T> beanType, String realClassName) {
        if (null == realClassName) {
            return getBean(beanType);
        }

        Class<?> realClass;
        try {
            realClass = Class.forName(realClassName);
        } catch (ClassNotFoundException e) {
            throw new InitializeInvokerException(e);
        }

        if (!realClass.isAssignableFrom(beanType)) {
            throw new InitializeInvokerException(realClassName + " can not be convert to " + beanType.getName());
        }

        @SuppressWarnings("unchecked")
        T bean = (T) getBean(realClass);
        return bean;
    }

    public <T> T getBean(Class<T> beanType) {
        T bean;
        try {
            bean = applicationContext.getBean(beanType);
        } catch (Exception e) {
            throw new InitializeInvokerException("cannot find bean class is" + beanType);
        }
        return bean;
    }

    /**
     * 获取协议对应代理
     * @param protocol
     * @return
     */
    public <T> T getProtocolInvoke(String protocol, Class<T> beanType) {
        T bean;
        try {
            bean = applicationContext.getBean(protocol.concat("Invoke").intern(), beanType);
        } catch (Exception e) {
            throw new InitializeInvokerException("cannot find InvokeProxy by " + protocol);
        }
        return bean;
    }
}
