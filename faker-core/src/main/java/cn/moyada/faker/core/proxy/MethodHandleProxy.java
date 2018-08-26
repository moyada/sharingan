package cn.moyada.faker.core.proxy;


import cn.moyada.faker.common.exception.InitializeInvokerException;
import cn.moyada.faker.common.model.MethodProxy;
import cn.moyada.faker.core.convert.AppInfoConverter;
import cn.moyada.faker.core.handler.AbstractHandler;
import cn.moyada.faker.manager.FakerManager;
import cn.moyada.faker.manager.domain.AppInfoDO;
import cn.moyada.faker.manager.domain.MethodInvokeDO;
import cn.moyada.faker.module.Dependency;
import cn.moyada.faker.module.loader.ModuleFetch;
import com.alibaba.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;
import java.util.List;

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
    private ModuleFetch moduleFetch;

    @Autowired
    private AbstractHandler handle;

    public MethodProxy getProxy(MethodInvokeDO invokeInfo) {
        MethodProxy proxy;

        log.info("init method proxy info.");
        Dependency dependency = getDependency(invokeInfo.getAppId());

        // 获取参数类型
        Class<?>[] paramTypes = getParamClass(dependency, invokeInfo.getParamType());

        // 获取方法句柄
        MethodHandle methodHandle = handle.fetchHandleInfo(dependency, invokeInfo.getClassName(),
                invokeInfo.getMethodName(), invokeInfo.getReturnType(), paramTypes);

        // 获取接口
        Class<?> classType;
        try {
            classType = moduleFetch.getClass(dependency, invokeInfo.getClassName());
        } catch (ClassNotFoundException e) {
            log.error("fetch service class error: " + e.getLocalizedMessage());
            throw new InitializeInvokerException("获取结果失败: " + invokeInfo.getClassName());
        }

        proxy = new MethodProxy();
        proxy.setParamTypes(paramTypes);
        proxy.setMethodHandle(methodHandle);
        proxy.setService(serviceAssembly);
        return proxy;
    }

    private Dependency getDependency(int appId) {
        AppInfoDO appInfoDO = fakerManager.getDependencyByAppId(appId);
        if(null == appInfoDO) {
            throw new InitializeInvokerException("获取应用信息失败: " + appId);
        }

        Dependency dependency = AppInfoConverter.toDependency(appInfoDO);
        String dependencies = appInfoDO.getDependencies();
        List<AppInfoDO> appList = fakerManager.getDependencyByAppId(dependencies);
        dependency.setDependencyList(AppInfoConverter.toDependency(appList));
        return dependency;
    }

    private Class[] getParamClass(Dependency dependency, String paramType) {
        String[] argsType = paramType.split(",");
        int length = argsType.length;
        Class<?>[] paramTypes = new Class[length];
        for (int index = 0; index < length; index++) {
            try {
                paramTypes[index] = moduleFetch.getClass(dependency, argsType[index]);
            } catch (ClassNotFoundException e) {
                log.error("fetch service method error: " + e.getLocalizedMessage());
                throw new InitializeInvokerException("获取参数类型失败: " + argsType[index]);
            }
        }
        return paramTypes;
    }
}
