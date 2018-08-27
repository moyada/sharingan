package cn.moyada.faker.module.handler;


import cn.moyada.faker.common.exception.InitializeInvokerException;
import cn.moyada.faker.module.Dependency;
import cn.moyada.faker.module.InvokeInfo;
import cn.moyada.faker.module.InvokeMetadata;
import cn.moyada.faker.module.fetch.MetadataFetch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 调用代理生成器
 * @author xueyikang
 * @create 2017-12-31 16:02
 */
@Component
public class MetadataWrapper {
    private static final Logger log = LoggerFactory.getLogger(MetadataWrapper.class);

    @Autowired
    private MetadataFetch metadataFetch;

    public InvokeMetadata getProxy(Dependency dependency, InvokeInfo invokeInfo) {
        log.info("init method proxy info.");

        // 获取参数类型
        Class clazz;
        Class<?>[] paramTypes;
        Class returnType;

        try {
            clazz = metadataFetch.getClass(dependency, invokeInfo.getClassType());

            paramTypes = getParamClass(dependency, invokeInfo.getParamType());

            returnType = metadataFetch.getClass(dependency, invokeInfo.getReturnType());
        } catch (ClassNotFoundException e) {
            throw new InitializeInvokerException("类加载失败: " + e.getMessage());
        }

        // 获取方法句柄
        MethodHandle methodHandle = getMethodHandle(dependency, clazz, invokeInfo.getMethodName(), paramTypes, returnType);

        InvokeMetadata metadata = new InvokeMetadata();
        metadata.setClassType(clazz);
        metadata.setMethodHandle(methodHandle);
        metadata.setParamTypes(paramTypes);
        metadata.setReturnType(returnType);
        return metadata;
    }

    private Class[] getParamClass(Dependency dependency, String paramType) throws ClassNotFoundException {
        String[] argsType = paramType.split(",");
        int length = argsType.length;
        Class<?>[] paramTypes = new Class[length];
        for (int index = 0; index < length; index++) {
            paramTypes[index] = metadataFetch.getClass(dependency, argsType[index]);
        }
        return paramTypes;
    }

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
            e.printStackTrace();
            throw new InitializeInvokerException("方法句柄获取失败: " + e.getMessage());
        }
        return methodHandle;
    }
}
