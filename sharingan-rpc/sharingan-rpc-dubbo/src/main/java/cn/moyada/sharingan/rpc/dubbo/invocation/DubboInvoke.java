package cn.moyada.sharingan.rpc.dubbo.invocation;

import cn.moyada.sharingan.common.exception.InstanceNotFountException;
import cn.moyada.sharingan.common.utils.StringUtil;
import cn.moyada.sharingan.rpc.api.config.DefaultConfig;
import cn.moyada.sharingan.rpc.api.invoke.AsyncInvoke;
import cn.moyada.sharingan.rpc.api.invoke.AsyncMethodInvoke;
import cn.moyada.sharingan.rpc.api.invoke.InvocationMetaDate;
import cn.moyada.sharingan.rpc.api.invoke.InvokeProxy;
import cn.moyada.sharingan.rpc.dubbo.config.DubboConfig;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("dubbo")
public class DubboInvoke extends AsyncMethodInvoke implements AsyncInvoke, InvokeProxy {

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
            return;
        }

        // 当前应用配置
        config = new ApplicationConfig();
        config.setName(defaultConfig.getIdentifyName());

        // 连接注册中心配置
        registry = new RegistryConfig();
        registry.setProtocol("dubbo");
        registry.setAddress(dubboConfig.getRegistry());
        registry.setPort(-1);
        registry.setRegister(false);
        registry.setSubscribe(true);
        registry.setUsername(dubboConfig.getUsername());
        registry.setPassword(dubboConfig.getPassword());

        consumer = new ConsumerConfig();
        consumer.setTimeout(3000);
        consumer.setActives(100);
        consumer.setLazy(false);
    }

    @Override
    public void initialization(InvocationMetaDate metaDate) throws InstanceNotFountException {
        this.methodHandle = metaDate.getMethodHandle();

        ReferenceConfig<?> reference = new ReferenceConfig<>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setApplication(config);
        reference.setConsumer(consumer);
        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
        reference.setInterface(metaDate.getService());

        try {
            this.instance = reference.get();
        }
        catch (Exception e) {
            throw new InstanceNotFountException(e.getMessage());
        }
    }
}
