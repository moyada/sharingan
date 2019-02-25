package io.moyada.sharingan.rpc.sofa;


import com.alipay.sofa.rpc.config.ApplicationConfig;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import io.moyada.sharingan.infrastructure.ContextFactory;
import io.moyada.sharingan.infrastructure.config.DefaultConfig;
import io.moyada.sharingan.infrastructure.exception.InstanceNotFountException;
import io.moyada.sharingan.infrastructure.invoke.DefaultMethodInvoke;
import io.moyada.sharingan.infrastructure.invoke.data.ClassInvocation;
import io.moyada.sharingan.infrastructure.util.ClassUtil;
import io.moyada.sharingan.infrastructure.util.StringUtil;
import io.moyada.sharingan.rpc.sofa.config.SofaConfig;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * sofa 协议调用器
 * @author xueyikang
 * @since 0.0.1
 **/
public class SofaInvoke extends DefaultMethodInvoke<ClassInvocation> {

    @Autowired
    private ContextFactory contextFactory;

    @Autowired
    private SofaConfig sofaConfig;

    @Autowired
    private DefaultConfig defaultConfig;

    private ApplicationConfig applicationConfig;
    private List<RegistryConfig> registryConfigs;

    private ConsumerConfig<?> consumerConfig;

    @PostConstruct
    public void initConfig() {
        if (null == sofaConfig.getRegistry() && StringUtil.isEmpty(sofaConfig.getDirectUrl())) {
            // 无效注册中心，销毁实例
            contextFactory.destroyBean(SofaAutoConfiguration.BEAN_NAME);
            return;
        }

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setAppName(defaultConfig.getIdentifyName());

        List<RegistryConfig> registryConfigs = new ArrayList<>();
        for (String registry : sofaConfig.getRegistry()) {
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setRegister(false);
            registryConfig.setSubscribe(false);
            registryConfig.setProtocol(sofaConfig.getProtocol());
            registryConfig.setAddress(registry);
            registryConfig.setConnectTimeout(sofaConfig.getTimeout());
            registryConfig.setTimeout(sofaConfig.getTimeout());
            registryConfigs.add(registryConfig);
        }

        this.applicationConfig = applicationConfig;
        this.registryConfigs = registryConfigs;
    }

    @Override
    protected void doInitialize(ClassInvocation metaDate) throws InstanceNotFountException {
        ConsumerConfig consumerConfig = new ConsumerConfig<>()
                .setApplication(this.applicationConfig)
                .setInterfaceId(metaDate.getClassType().getName()) // 指定接口
                .setGeneric(ClassUtil.isGeneric(metaDate.getClassType()))
                .setProtocol(sofaConfig.getProtocol()) // 指定协议
                .setConcurrents(200)
                .setRetries(0)
                .setDirectUrl(sofaConfig.getDirectUrl()) // 指定直连地址
                .setRegistry(this.registryConfigs)
                .setSubscribe(false)
                .setRegister(false)
                .setConnectTimeout(sofaConfig.getTimeout())
                .setTimeout(sofaConfig.getTimeout());

        Object ref;
        try {
            ref = consumerConfig.refer();
        }
        catch (Exception e) {
            throw new InstanceNotFountException(e);
        }

        setInstance(ref);
        setMethodHandle(metaDate.getMethodHandle());
        this.consumerConfig = consumerConfig;
    }

    @Override
    public void destroy() {
        setInstance(null);
        setMethodHandle(null);
        if (this.consumerConfig != null) {
            this.consumerConfig.unRefer();
            this.consumerConfig = null;
        }
    }

    @Override
    protected void beforeInvoke() {
        invoke(null);
    }
}
