package io.moyada.sharingan.rpc.dubbo;


import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import io.moyada.sharingan.infrastructure.config.DefaultConfig;
import io.moyada.sharingan.infrastructure.exception.InstanceNotFountException;
import io.moyada.sharingan.infrastructure.invoke.DefaultMethodInvoke;
import io.moyada.sharingan.infrastructure.invoke.Invocation;
import io.moyada.sharingan.infrastructure.invoke.data.ClassInvocation;
import io.moyada.sharingan.infrastructure.util.ClassUtil;
import io.moyada.sharingan.rpc.dubbo.config.DubboConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * dubbo协议调用器
 * @author xueyikang
 * @since 0.0.1
 **/
public class DubboInvoke extends DefaultMethodInvoke<ClassInvocation> {

    private ApplicationConfig config;
    private ConsumerConfig consumer;
    private List<RegistryConfig> registries;

    private ReferenceConfig<?> reference;

    private Invocation preInvocation;

    public DubboInvoke(DefaultConfig defaultConfig, DubboConfig dubboConfig) {
        // 当前应用配置
        ApplicationConfig config = new ApplicationConfig();
        config.setName(defaultConfig.getIdentifyName());

        // 消费规则
        ConsumerConfig consumer = new ConsumerConfig();
        consumer.setTimeout(dubboConfig.getTimeout());
        consumer.setActives(100);
        consumer.setLazy(false);
        consumer.setRetries(0);

        // 连接注册中心配置
        List<RegistryConfig> registries = new ArrayList<>();
        for (String address : dubboConfig.getRegistry()) {
            RegistryConfig registry = new RegistryConfig();
            registry.setProtocol(dubboConfig.getProtocol());
            registry.setAddress(address);
            registry.setPort(-1);
            registry.setRegister(false);
            registry.setSubscribe(true);
            registry.setUsername(dubboConfig.getUsername());
            registry.setPassword(dubboConfig.getPassword());
            registry.setTimeout(dubboConfig.getTimeout());

            registries.add(registry);
        }

        this.config = config;
        this.consumer = consumer;
        this.registries = registries;
    }

    @Override
    protected void doInitialize(ClassInvocation metaDate) throws InstanceNotFountException {
        ReferenceConfig<?> reference = new ReferenceConfig<>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setApplication(config);
        reference.setConsumer(consumer);
        reference.setRegistries(registries);
        reference.setRetries(0);
        reference.setInterface(metaDate.getClassType());

        Object ref;

        try {
            ref = reference.get();
        }
        catch (Exception e) {
            throw new InstanceNotFountException(e);
        }

        setInstance(ref);
        setMethodHandle(metaDate.getMethodHandle());
        this.reference = reference;
    }

    @Override
    public void destroy() {
        setInstance(null);
        setMethodHandle(null);
        if (this.reference != null) {
            this.reference.destroy();
            this.reference = null;
        }
    }

    @Override
    protected String convertError(Throwable throwable) {
        Throwable cause = throwable.getCause();
        String message = null == cause ? throwable.getMessage() : cause.getMessage();

        int index = message.indexOf("cause:");
        if (index < 0) {
            return message;
        }

        int end = message.indexOf('\n', index);
        if (end < 0 || end - index < 7) {
            return message.substring(index);
        }
        return message.substring(index + 7, end);
    }

    @Override
    protected void beforeInvoke(ClassInvocation metaDate) {
        invoke(ClassUtil.newInstance(metaDate.getParamTypes()));
    }
}
