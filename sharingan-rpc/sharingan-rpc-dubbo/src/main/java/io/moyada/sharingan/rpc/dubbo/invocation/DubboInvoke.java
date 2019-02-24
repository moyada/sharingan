package io.moyada.sharingan.rpc.dubbo.invocation;


import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import io.moyada.sharingan.infrastructure.ContextFactory;
import io.moyada.sharingan.infrastructure.config.DefaultConfig;
import io.moyada.sharingan.infrastructure.exception.InstanceNotFountException;
import io.moyada.sharingan.infrastructure.invoke.DefaultMethodInvoke;
import io.moyada.sharingan.infrastructure.invoke.data.ClassInvocation;
import io.moyada.sharingan.infrastructure.util.StringUtil;
import io.moyada.sharingan.rpc.dubbo.DubboAutoConfiguration;
import io.moyada.sharingan.rpc.dubbo.config.DubboConfig;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * dubbo协议调用器
 * @author xueyikang
 * @since 0.0.1
 **/
public class DubboInvoke extends DefaultMethodInvoke<ClassInvocation> {

    @Autowired
    private ContextFactory contextFactory;

    @Autowired
    private DubboConfig dubboConfig;

    @Autowired
    private DefaultConfig defaultConfig;

    private ApplicationConfig config;
    private RegistryConfig registry;
    private ConsumerConfig consumer;

    @PostConstruct
    public void initConfig() {
        if (StringUtil.isEmpty(dubboConfig.getRegistry())) {
            // 无效注册中心，销毁实例
            contextFactory.destroyBean(DubboAutoConfiguration.BEAN_NAME);
            return;
        }

        // 当前应用配置
        config = new ApplicationConfig();
        config.setName(defaultConfig.getIdentifyName());

        // 连接注册中心配置
        registry = new RegistryConfig();
        registry.setProtocol(dubboConfig.getProtocol());
        registry.setAddress(dubboConfig.getRegistry());
        registry.setPort(-1);
        registry.setRegister(false);
        registry.setSubscribe(true);
        registry.setUsername(dubboConfig.getUsername());
        registry.setPassword(dubboConfig.getPassword());

        // 消费规则
        consumer = new ConsumerConfig();
        consumer.setTimeout(dubboConfig.getTimeout());
        consumer.setActives(100);
        consumer.setLazy(false);
        consumer.setRetries(0);
    }

    @Override
    protected void doInitialize(ClassInvocation metaDate) throws InstanceNotFountException {
        setMethodHandle(metaDate.getMethodHandle());

        ReferenceConfig<?> reference = new ReferenceConfig<>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setApplication(config);
        reference.setConsumer(consumer);
        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
        reference.setInterface(metaDate.getClassType());

        Object ref;

        try {
            ref = reference.get();
        }
        catch (Exception e) {
            throw new InstanceNotFountException(e);
        }
        setInstance(ref);
    }

    @Override
    protected void beforeInvoke() {
        invoke(null);
    }
}
