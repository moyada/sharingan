package cn.moyada.sharingan.spring.boot.autoconfigure;

import cn.moyada.sharingan.instrument.boost.ClassUtil;
import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistInheritProxy;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistProxy;
import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.api.annotation.Exclusive;
import cn.moyada.sharingan.monitor.api.annotation.Listener;
import cn.moyada.sharingan.monitor.api.entity.DefaultInvocation;
import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.spring.boot.autoconfigure.util.BeanDefinitionUtil;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    private JavassistProxy<Invocation> javassistProxy;

    public MonitorBeanDefinitionScanner(BeanDefinitionRegistry registry, Map<String, Object> attach) {
        super(registry);

        try {
            javassistProxy = new JavassistInheritProxy<>(Monitor.class,
                    Monitor.class.getDeclaredMethod("listener", Invocation.class),
                    Invocation.class, DefaultInvocation.class, attach, Variables.INJECT_APP_NAME);
        } catch (NoSuchMethodException e) {
            this.logger.error(e.getMessage());
        }
    }

    @Override
    public void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        this.addIncludeFilter(new AnnotationTypeFilter(Listener.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        processBeanDefinitions(beanDefinitions);
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        for (BeanDefinitionHolder holder : beanDefinitions) {
            AbstractBeanDefinition definition = (AbstractBeanDefinition) holder.getBeanDefinition();
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
                this.logger.error(e.getMessage());
                continue;
            }

            // 修改 BeanDefinition 类信息
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Creating MonitorProxy Wrapper Bean name '" + holder.getBeanName() + "', type is '" + definition.getBeanClassName() + "'");
            }
            definition.setBeanClass(beanClass);
        }
    }

    @Override
    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition) && beanDefinition.getMetadata()
                .hasAnnotation(Listener.class.getName()) && beanDefinition.getMetadata()
                .hasAnnotation(Component.class.getName());
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        return this.getRegistry().containsBeanDefinition(beanName);
    }
}
