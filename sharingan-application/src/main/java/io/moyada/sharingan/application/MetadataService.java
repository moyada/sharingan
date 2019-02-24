package io.moyada.sharingan.application;

import io.moyada.sharingan.domain.metadada.*;
import io.moyada.sharingan.domain.request.QuestInfo;
import io.moyada.sharingan.infrastructure.exception.InitializeInvokerException;
import io.moyada.sharingan.infrastructure.module.Dependency;
import io.moyada.sharingan.infrastructure.module.MetadataFetch;
import io.moyada.sharingan.infrastructure.module.ModuleClassLoader;
import io.moyada.sharingan.infrastructure.util.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class MetadataService implements ApplicationContextAware {

    // 项目类加载器
    private ClassLoader originClassLoader;

    private MetadataFetch metadataFetch;

    private MetadataRepository metadataRepository;

    @Autowired
    public MetadataService(MetadataFetch metadataFetch, MetadataRepository metadataRepository) {
        this.metadataFetch = metadataFetch;
        this.metadataRepository = metadataRepository;
    }

    /**
     * 获取调用数据
     * @param questInfo
     * @return
     */
    public InvokeData getInvokeData(QuestInfo questInfo) {
        AppData appData = metadataRepository.findAppById(questInfo.getAppId());
        ServiceData serviceData = metadataRepository.findServiceById(questInfo.getServiceId());
        serviceData.setAppData(appData);

        InvokeData invokeData;

        Protocol protocol = serviceData.getProtocol();
        if (null == protocol) {
            throw new InitializeInvokerException(serviceData.getName() + " does not have protocol");
        }

        switch (protocol.getMode()) {
            case CLASS:
                MethodData methodData = metadataRepository.findMethodById(questInfo.getFunctionId());
                invokeData = getClassData(appData, methodData);
                break;
            case HTTP:
                invokeData = metadataRepository.findHttpById(questInfo.getFunctionId());
                break;
            default:
                throw new InitializeInvokerException(serviceData.getName() + " protocol " + protocol.getMode().name() + " not have process.");
        }

        invokeData.setServiceData(serviceData);
        return invokeData;
    }

    /**
     * 获取类信息
     * @param appData
     * @param methodData
     * @return
     */
    public ClassData getClassData(AppData appData, MethodData methodData) {
        Dependency dependency = getDependency(appData);
        loadMetadata(dependency);
        return getClassType(dependency, methodData);
    }

    public void reset() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader instanceof ModuleClassLoader) {
            ((ModuleClassLoader) classLoader).destroy();
        }
        Thread.currentThread().setContextClassLoader(originClassLoader);
    }

    private Dependency getDependency(AppData appData) {
        Dependency dependency = appData.getDependency();
        int[] appIds = appData.getDependencies();
        if (null == appIds) {
            return dependency;
        }

        List<AppData> apps = metadataRepository.findAppByIds(appIds);
        if (null == apps) {
            return dependency;
        }
        dependency.setDependencyList(apps.stream().map(AppData::getDependency).collect(Collectors.toList()));
        return dependency;
    }

    private void loadMetadata(Dependency dependency) {
        if (null == dependency) {
            return;
        }
        ClassLoader classLoader = metadataFetch.getClassLoader(dependency);
        if(null == classLoader) {
            return;
        }
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    /**
     * 包装调用信息
     * @param dependency
     * @param methodData
     * @return
     */
    private ClassData getClassType(Dependency dependency, MethodData methodData) {

        // 获取参数类型
        Class clazz;
        Class<?>[] paramTypes;
        Class returnType;

        try {
            clazz = metadataFetch.getClass(dependency, methodData.getClassName());
            paramTypes = getParamClass(dependency, methodData.getParamType());
            returnType = metadataFetch.getClass(dependency, methodData.getReturnType());
        } catch (ClassNotFoundException e) {
            throw new InitializeInvokerException(e.getMessage());
        }

        MethodHandle methodHandle = getMethodHandle(dependency, clazz, methodData.getMethodName(), paramTypes, returnType);
        return new ClassData(methodData.getMethodName(), clazz, paramTypes, returnType, methodHandle);
    }

    private Class<?>[] getParamClass(Dependency dependency, String paramType) throws ClassNotFoundException {
        if (StringUtil.isEmpty(paramType)) {
            return new Class<?>[0];
        }
        String[] argsType = paramType.split(",");
        int length = argsType.length;
        Class<?>[] paramTypes = new Class[length];
        for (int index = 0; index < length; index++) {
            paramTypes[index] = metadataFetch.getClass(dependency, argsType[index]);
        }
        return paramTypes;
    }

    /**
     * 获取方法句柄
     * @param dependency
     * @param classType
     * @param methodName
     * @param paramClass
     * @param returnType
     * @return
     */
    private MethodHandle getMethodHandle(Dependency dependency, Class classType, String methodName,
                                         Class[] paramClass, Class returnType) {

        MethodHandle methodHandle;
        MethodHandles.Lookup lookup = metadataFetch.getMethodLookup(dependency);
        try {
            // 创建方法信息
            MethodType methodType = MethodType.methodType(returnType, paramClass);
            // 查询方法返回方法具柄
            methodHandle = lookup.findVirtual(classType, methodName, methodType);
        } catch (NoSuchMethodException e) {
            throw new InitializeInvokerException("方法不存在: " + methodName);
        } catch (IllegalAccessException e) {
            throw new InitializeInvokerException("方法句柄获取失败: " + e.getMessage());
        }
        return methodHandle;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.originClassLoader = applicationContext.getClassLoader();
    }
}
