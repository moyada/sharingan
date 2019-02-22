package io.moyada.sharingan.spring.boot.autoconfigure;

import io.moyada.metadata.enhancement.Enhance;
import io.moyada.metadata.enhancement.EnhanceFactory;
import io.moyada.metadata.enhancement.statement.*;
import io.moyada.metadata.enhancement.support.Annotation;
import io.moyada.metadata.enhancement.support.Value;
import io.moyada.sharingan.monitor.api.Register;
import io.moyada.sharingan.monitor.api.entity.*;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Monitor;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.support.*;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    private final SharinganConfig sharinganConfig;
    private final Register register;
    private Integer appId;

    public MonitorBeanDefinitionScanner(BeanDefinitionRegistry registry, SharinganConfig sharinganConfig, Register register) {
        super(registry);
        this.sharinganConfig = sharinganConfig;
        if (null == sharinganConfig.getAttach()) {
            sharinganConfig.setAttach(Collections.emptyMap());
        }
        this.register = register;
    }

    @Override
    public void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(Monitor.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        processBeanDefinitions(beanDefinitions);
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        initAppInfo();

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

            initData(listenerInfo);
            // 修改 BeanDefinition 类信息
            Class<?> newBeanClass = proxy(beanClass, listenerInfo.getListenerMethods());

            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Creating MonitorProxy Wrapper Bean name '" + holder.getBeanName() + "', type is '" + definition.getBeanClassName() + "'");
            }
            definition.setBeanClass(newBeanClass);
        }
    }

    private void initAppInfo() {
        if (null == register) {
            return;
        }

        AppInfo appInfo = new AppInfo();
        appInfo.setName(sharinganConfig.getApplication());
        appInfo.setGroupId(sharinganConfig.getGroupId());
        appInfo.setArtifactId(sharinganConfig.getArtifactId());
        appId = register.addAppReturnId(appInfo);
    }

    private void initData(ListenerInfo listenerInfo) {
        if (null == register) {
            return;
        }

        Class classType = listenerInfo.getClassType();

        ServiceInfo serviceInfo = new ServiceInfo(appId, listenerInfo.getServiceName());
        serviceInfo.setProtocol(listenerInfo.getProtocol());
        serviceInfo.setClassType(classType);
        int serviceId = register.addServiceReturnId(serviceInfo);

        initData(serviceId, classType, listenerInfo.getListenerMethods());
    }

    private void initData(int serviceId, Class serviceClass, Collection<ListenerMethod> listenerMethods) {
        for (ListenerMethod listenerMethod : listenerMethods) {
            if (!listenerMethod.isNeedRegister()) {
                continue;
            }

            HttpData httpData = listenerMethod.getHttpData();
            if (null != httpData) {
                HttpInfo httpInfo = new HttpInfo(appId, serviceId, httpData.getMethodName());
                httpInfo.setType(httpData.getType());
                httpInfo.setParam(httpData.getParam());
                httpInfo.setHeader(httpData.getHeader());
                register.addHttpReturnId(httpInfo);
            } else {
                FunctionInfo FunctionInfo = new FunctionInfo(appId, serviceId, listenerMethod.getMethodName());
                FunctionInfo.setClassType(serviceClass);
                FunctionInfo.setParamTypes(listenerMethod.getParamTypes());
                FunctionInfo.setReturnType(listenerMethod.getReturnType());
                register.addFunctionReturnId(FunctionInfo);
            }
        }
    }

    private Class<?> proxy(Class<?> targetClass, Collection<ListenerMethod> listenerMethods) {
        String monitorName = "_monitor";
        IdentStatement monitor = IdentStatement.of(monitorName);

        Enhance<?> enhance = EnhanceFactory.extend(targetClass)
                .addImport(Invocation.class.getName())
                .addImport(DefaultInvocation.class.getName())
                .addField(monitorName, io.moyada.sharingan.monitor.api.Monitor.class, Modifier.PRIVATE, Annotation.of(Resource.class));

        boolean hasBoost = false;
        for (ListenerMethod method : listenerMethods) {
            if (method.getDomain() == null) {
                continue;
            }

            enhance.beforeMethod(method.getMethodName(), method.getParamTypes(),
                    BodyStatement.init()
                            .addStatement(
                                    IfStatement.If(
                                            new ConditionStatement(ConditionType.NE, IdentStatement.of(monitorName), IdentStatement.NULL),
                                            getListenerBody(method, monitor))
                            )
            );
            hasBoost = true;
        }

        if (hasBoost) {
            return enhance.create();
        }
        return targetClass;
    }

    private BodyStatement getListenerBody(ListenerMethod method, IdentStatement monitor) {
        String varName = "_var";
        IdentStatement var = IdentStatement.of(varName);

        BodyStatement listenerBody = BodyStatement.init()
                // 创建对象
                .addStatement(new VariableStatement(Invocation.class, varName, Value.newObject(DefaultInvocation.class)));

        // 应用
        listenerBody.addStatement(InvokeStatement.of(var, "setApplication", Value.of(sharinganConfig.getApplication())));
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
        for (Map.Entry<String, String> entry : sharinganConfig.getAttach().entrySet()) {
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
                 beanDefinition.getMetadata().hasAnnotation(Monitor.class.getName());
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        return this.getRegistry().containsBeanDefinition(beanName);
    }
}
