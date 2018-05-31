package cn.moyada.dubbo.faker.core.common;

import cn.moyada.dubbo.faker.core.loader.AppClassLoader;
import cn.moyada.dubbo.faker.core.utils.SoftReferenceUtil;
import com.alibaba.dubbo.common.bytecode.Proxy;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xueyikang
 * @create 2018-03-27 13:09
 */
@Component
@Lazy(false)
public class BeanHolder implements ApplicationContextAware {

//    @Autowired
//    private ClassLoaderAspect classLoaderAspect;

    private static volatile Map<Class<?>, SoftReference<ReferenceConfig<?>>> beanMap;

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

    public Object getDubboBean(AppClassLoader classLoader, Class<?> cls) {
        ReferenceConfig<?> reference = SoftReferenceUtil.get(beanMap, cls);
        if(null != reference) {
            return reference.get();
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

        Object service;
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
        SoftReferenceUtil.put(beanMap, cls, reference);
        return service;
    }

    public static URLClassLoader getSpringClassLoader() {
        return (URLClassLoader) applicationContext.getClassLoader();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanHolder.applicationContext = applicationContext;
    }

//    static {
//        try {
//            replaceGetProxy();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 动态修改Dubbo Proxy getProxy方法
     */
    @SuppressWarnings("unchecked")
    protected static void replaceGetProxy() throws NotFoundException, CannotCompileException, NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException {
        String className = "com.alibaba.dubbo.common.bytecode.Proxy";

        ClassPool classPool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(Proxy.class);
        classPool.insertClassPath(classPath);

        CtClass ctClass = classPool.getCtClass(className);
        CtClass paramType = classPool.getCtClass("java.lang.Class[]");
        CtMethod ctMethod = ctClass.getDeclaredMethod("getProxy", new CtClass[]{paramType});

        // 替换为获取当前线程类加载器
        ctMethod.instrument(
                new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        m.replace("$_ = getProxy(Thread.currentThread().getContextClassLoader(), $sig);");
                    }
                });
        ctClass.writeFile();
        byte[] bytes = ctClass.toBytecode();

        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        defineClass.setAccessible(true);

        // 清除已加载类信息
        Class<?> proxyClass = Class.forName(className);

        URLClassLoader classLoader = (URLClassLoader) proxyClass.getClassLoader();
        URL[] urLs = classLoader.getURLs();

        URLClassLoader urlClassLoader = URLClassLoader.newInstance(urLs, classLoader.getParent());

//        Field classes = ClassLoader.class.getDeclaredField("classes");
//        classes.setAccessible(true);
//
//        Vector<Class<?>> oldClasses = (Vector<Class<?>>) classes.get(classLoader);
//        Vector<Class<?>> newClasses = new Vector<>(oldClasses.size());
//        for (Class<?> clazz : oldClasses) {
//            if(clazz.getName().equals(className)) {
//                continue;
//            }
//            newClasses.add(clazz);
//        }
//        classes.set(classLoader, newClasses);

        // 重新载入字节码
        defineClass.invoke(urlClassLoader, className, bytes, 0, bytes.length);
    }
}
