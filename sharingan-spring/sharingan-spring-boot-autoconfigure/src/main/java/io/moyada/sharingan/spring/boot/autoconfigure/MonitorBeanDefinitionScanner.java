package io.moyada.sharingan.spring.boot.autoconfigure;

import io.moyada.metadata.enhancement.Enhance;
import io.moyada.metadata.enhancement.EnhanceFactory;
import io.moyada.metadata.enhancement.statement.*;
import io.moyada.metadata.enhancement.support.Annotation;
import io.moyada.metadata.enhancement.support.Value;
import io.moyada.sharingan.monitor.api.entity.DefaultInvocation;
import io.moyada.sharingan.monitor.api.entity.Invocation;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Monitor;
import io.moyada.sharingan.spring.boot.autoconfigure.support.ListenerAnalyser;
import io.moyada.sharingan.spring.boot.autoconfigure.support.ListenerInfo;
import io.moyada.sharingan.spring.boot.autoconfigure.support.ListenerMethod;
import io.moyada.sharingan.spring.boot.autoconfigure.support.ProxyField;
import io.moyada.sharingan.spring.boot.autoconfigure.util.BeanDefinitionUtil;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.Resource;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    private final String application;
    private final Map<String, String> attach;

    public MonitorBeanDefinitionScanner(BeanDefinitionRegistry registry, String application, Map<String, String> attach) {
        super(registry);
        this.application = application;
        if (null == attach) {
            this.attach = Collections.emptyMap();
        } else {
            this.attach = attach;
        }
    }

    @Override
    public void registerDefaultFilters() {
//        this.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        this.addIncludeFilter(new AnnotationTypeFilter(Monitor.class));
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

            ListenerInfo listenerInfo = ListenerAnalyser.getListenerInfo(beanClass);
            if (null == listenerInfo) {
                continue;
            }

            // 修改 BeanDefinition 类信息
            Class<?> newBeanClass = proxy(beanClass, listenerInfo);

            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Creating MonitorProxy Wrapper Bean name '" + holder.getBeanName() + "', type is '" + definition.getBeanClassName() + "'");
            }
            definition.setBeanClass(newBeanClass);
        }
    }

    public Class<?> proxy(Class<?> targetClass, ListenerInfo listenerInfo) {
        String monitorName = "_monitor";
        IdentStatement monitor = IdentStatement.of(monitorName);

        Enhance<?> enhance = EnhanceFactory.extend(targetClass)
                .addImport(Invocation.class.getName())
                .addImport(DefaultInvocation.class.getName())
                .addField(monitorName, io.moyada.sharingan.monitor.api.Monitor.class, Modifier.PRIVATE, Annotation.of(Resource.class));

        for (ListenerMethod method : listenerInfo.getListenerMethods()) {
            enhance.beforeMethod(method.getMethodName(), method.getParamTypes(),
                    BodyStatement.init()
                            .addStatement(
                                    IfStatement.If(
                                            new ConditionStatement(ConditionType.NE, IdentStatement.of(monitorName), IdentStatement.NULL),
                                            getListenerBody(method, monitor))
                            )
            );
        }

        return enhance.create();
    }

    private BodyStatement getListenerBody(ListenerMethod method, IdentStatement monitor) {
        String varName = "_var";
        IdentStatement var = IdentStatement.of(varName);

        BodyStatement listenerBody = BodyStatement.init()
                // 创建对象
                .addStatement(new VariableStatement(Invocation.class, varName, Value.newObject(DefaultInvocation.class)));

        // 应用
        listenerBody.addStatement(InvokeStatement.of(var, "setApplication", Value.of(application)));
        // 业务领域
        listenerBody.addStatement(InvokeStatement.of(var, "setDomain", Value.of(method.getDomain())));
        // 序列化
        listenerBody.addStatement(InvokeStatement.of(var, "setSerializationType", new Value(method.getSerializationType())));

        // 参数
        for (ProxyField field : method.getProxyParams()) {
            listenerBody.addStatement(InvokeStatement.of(var, "addArgs",
                    Value.of(field.getParamName()), Value.of(IdentStatement.of(field.getParamIndex() + 1))));
        }

        // 附带信息
        for (Map.Entry<String, String> entry : attach.entrySet()) {
            listenerBody.addStatement(InvokeStatement.of(var, "addAttach",
                    Value.of(entry.getKey()), Value.of(entry.getValue())));
        }

        // 调用方法
        listenerBody.addStatement(InvokeStatement.of(monitor, "listener", Value.of(var)));

        return listenerBody;
    }

    @Override
    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition) &&
//                beanDefinition.getMetadata().hasAnnotation(Component.class.getName()) &&
                 beanDefinition.getMetadata().hasAnnotation(Monitor.class.getName());
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        return this.getRegistry().containsBeanDefinition(beanName);
    }
}
