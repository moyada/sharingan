package cn.moyada.sharingan.rpc.dubbo.invocation;

import cn.moyada.sharingan.common.exception.InstanceNotFountException;
import cn.moyada.sharingan.common.task.DestroyTask;
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

/**
 * dubbo协议调用器
 */
@Component("dubboInvoke")
public class DubboInvoke extends AsyncMethodInvoke implements AsyncInvoke, InvokeProxy {

    @Autowired
    private DestroyTask destroyTask;

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
            destroyTask.addDestroyBean("dubboInvoke");
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

        // 消费规则
        consumer = new ConsumerConfig();
        consumer.setTimeout(3000);
        consumer.setActives(100);
        consumer.setLazy(false);
        consumer.setRetries(0);
    }

    @Override
    public void initialize(InvocationMetaDate metaDate) throws InstanceNotFountException {
        InvocationMetaDate.ClassInfo classInfo = metaDate.getClassInfo();

        this.methodHandle = classInfo.getMethodHandle();

        ReferenceConfig<?> reference = new ReferenceConfig<>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setApplication(config);
        reference.setConsumer(consumer);
        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
        reference.setInterface(classInfo.getServiceClass());

        try {
            this.instance = reference.get();
        }
        catch (IllegalArgumentException e) {
            throw new InstanceNotFountException(e);
        }
        catch (RuntimeException e) {
            throw new InstanceNotFountException(e);
        }
        catch (Exception e) {
            throw new InstanceNotFountException(e);
        }
    }
}
