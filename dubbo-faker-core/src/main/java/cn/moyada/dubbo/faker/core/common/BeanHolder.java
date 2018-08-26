package cn.moyada.dubbo.faker.core.common;

import cn.moyada.dubbo.faker.core.loader.AppClassLoader;
import cn.moyada.dubbo.faker.core.model.CleanerReference;
import cn.moyada.dubbo.faker.core.utils.CleanerUtil;
import cn.moyada.dubbo.faker.core.utils.SoftReferenceUtil;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 实例管理器
 * @author xueyikang
 * @create 2018-03-27 13:09
 */
@Component
@Lazy(false)
public class BeanHolder implements ApplicationContextAware {

    private static volatile Map<Class<?>, SoftReference<CleanerReference<ReferenceConfig<?>>>> beanMap;

    private static ApplicationContext applicationContext;

    private final ApplicationConfig config;
    private final RegistryConfig registry;
    private final ConsumerConfig consumer;

    public BeanHolder(@Value("${dubbo.application}") String appName,
                      @Value("${dubbo.registry}") String address,
                      @Value("${dubbo.logger}") String logger,
                      @Value("${dubbo.username}") String username,
                      @Value("${dubbo.password}") String password) {
        if(null == address) {
            throw new NullPointerException("dubbo.registry can not be null.");
        }
        // 当前应用配置
        config = new ApplicationConfig();
        config.setName(appName);
        config.setLogger(logger);

        // 连接注册中心配置
        registry = new RegistryConfig();
        registry.setProtocol("dubbo");
        registry.setAddress(address);
        registry.setPort(-1);
        registry.setUsername(username);
        registry.setPassword(password);

        consumer = new ConsumerConfig();
        consumer.setTimeout(3000);
        consumer.setActives(100);
        consumer.setLazy(false);

        beanMap = new HashMap<>();
    }

    /**
     * 获取spring实例
     * @param cls
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> cls) throws BeansException {
        T bean;
        try {
            bean = applicationContext.getBean(cls);
        }
        catch (Exception e) {
            e.printStackTrace();
            bean = null;
        }
        if(null == bean) {
            throw new NullPointerException(cls.getName() + "can not find any bean.");
        }
        return bean;
    }

    /**
     * 获取dubbo实例
     * @param classLoader
     * @param cls
     * @return
     */
    public Object getDubboBean(AppClassLoader classLoader, Class<?> cls) {
        Object service;
        ReferenceConfig<?> reference;
        CleanerReference<ReferenceConfig<?>> cleanerReference = SoftReferenceUtil.get(beanMap, cls);
        if(null != cleanerReference) {
            reference = cleanerReference.getReference();
            if (null != reference && (service = reference.get()) != null) {
                return service;
            }
            CleanerUtil.cleaner(cleanerReference);
        }

        reference = new ReferenceConfig<>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setApplication(config);
        reference.setConsumer(consumer);
        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
        reference.setInterface(cls);

        //获取dubbo提供的缓存
//        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        // cache.get方法中会缓存 reference对象，并且调用reference.get方法启动ReferenceConfig，并返回经过代理后的服务接口的对象
//        Object service = cache.get(reference);

        try {
            // 切换类加载器
            Thread.currentThread().setContextClassLoader(classLoader);
            service = reference.get();
        }
        catch (Exception e) {
            e.printStackTrace();
            service = null;
        }
//        finally {
//            Thread.currentThread().setContextClassLoader(contextClassLoader);
//        }
        if(null != service) {
            cleanerReference = CleanerUtil.create(reference);
            SoftReferenceUtil.put(beanMap, cls, cleanerReference);
        }
        return service;
    }

    public static URLClassLoader getSpringClassLoader() {
        return (URLClassLoader) applicationContext.getClassLoader();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanHolder.applicationContext = applicationContext;
    }
}
