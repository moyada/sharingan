package cn.moyada.dubbo.faker.core.proxy;

import cn.moyada.dubbo.faker.core.loader.Dependency;
import cn.moyada.dubbo.faker.core.loader.ModuleLoader;
import cn.moyada.dubbo.faker.core.common.BeanHolder;
import cn.moyada.dubbo.faker.core.exception.InitializeInvokerException;
import cn.moyada.dubbo.faker.core.handler.AbstractHandler;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.model.domain.AppInfoDO;
import cn.moyada.dubbo.faker.core.model.domain.MethodInvokeDO;
import com.alibaba.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;

/**
 * 调用代理生成器
 * @author xueyikang
 * @create 2017-12-31 16:02
 */
@Component
public class MethodHandleProxy {
    private static final Logger log = LoggerFactory.getLogger(MethodHandleProxy.class);

    @Autowired
    private FakerManager fakerManager;

    @Autowired
    private ModuleLoader moduleLoader;

    @Autowired
    private BeanHolder beanHolder;

    @Autowired
    private AbstractHandler handle;

    public MethodProxy getProxy(MethodInvokeDO invokeInfo, int poolSize) {
        MethodProxy proxy;

        log.info("init method proxy info.");

        AppInfoDO appInfo = fakerManager.getAppById(invokeInfo.getAppId());
        if(null == appInfo) {
            throw new InitializeInvokerException("获取应用信息失败: " + invokeInfo.getAppId());
        }
        Dependency dependency = new Dependency(appInfo.getGroupId(), appInfo.getArtifactId(), appInfo.getVersion(), appInfo.getUrl());

        // 获取参数类型
        Class<?>[] paramTypes;
        String[] argsType = invokeInfo.getParamType().split(",");
        int length = argsType.length;
        if(0 == length) {
            paramTypes = new Class[0];
        }
        else {
            paramTypes = new Class[length];
            for (int index = 0; index < length; index++) {
                try {
                    paramTypes[index] = moduleLoader.getClass(dependency, argsType[index]);
                } catch (ClassNotFoundException e) {
                    log.error("fetch service method error: " + e.getLocalizedMessage());
                    throw new InitializeInvokerException("获取参数类型失败: " + argsType[index]);
                }
            }
        }

        // 获取方法句柄
//        MethodHandle methodHandle = handle.fetchHandleInfo(invokeInfo.getClassName(),
//                invokeInfo.getMethodName(), invokeInfo.getReturnType(), paramTypes);
        MethodHandle[] methodHandle = new MethodHandle[poolSize];
        for (int index = 0; index < poolSize; index++) {
            methodHandle[index] = handle.fetchHandleInfo(dependency, invokeInfo.getClassName(),
                    invokeInfo.getMethodName(), invokeInfo.getReturnType(), paramTypes);
        }

        // 获取接口
        Class<?> classType;
        try {
            classType = moduleLoader.getClass(dependency, invokeInfo.getClassName());
        } catch (ClassNotFoundException e) {
            log.error("fetch service class error: " + e.getLocalizedMessage());
            throw new InitializeInvokerException("获取结果失败: " + invokeInfo.getClassName());
        }

        // 获取接口实例
        Object serviceAssembly;
//        Object[] serviceAssembly = new Object[poolSize];
        try {
            serviceAssembly = beanHolder.getDubboBean(moduleLoader.getClassLoader(dependency), classType);
//            for (int index = 0; index < poolSize; index++) {
//                serviceAssembly[index] = beanHolder.getService(index, classType);
//            }
        }
        catch (BeansException e) {
            log.error("fetch service bean error: " + e.getLocalizedMessage());
            throw new RpcException("获取接口实例失败: " + invokeInfo.getClassName(), e);
        }
        if(null == serviceAssembly) {
            log.error("fetch service bean error: " + invokeInfo.getClassName());
            throw new RpcException("获取接口实例失败: " + invokeInfo.getClassName());
        }

//        InvokerProxy[] invokerProxy = new InvokerProxy[poolSize];
        // 初始化服务注册
//        for (int index = 0; index < poolSize; index++) {
            try {
                methodHandle[0].invoke(serviceAssembly, null);
            } catch (Throwable throwable) {
            }
//            finally {
//                invokerProxy[index] = new InvokerProxy(methodHandle[index], serviceAssembly);
//            }
//        }

        proxy = new MethodProxy();
        proxy.setParamTypes(paramTypes);
        proxy.setMethodHandle(methodHandle);
        proxy.setService(serviceAssembly);
        return proxy;
    }
}
