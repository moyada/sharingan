package cn.moyada.faker.core.handler;


import cn.moyada.faker.common.exception.InitializeInvokerException;
import cn.moyada.faker.module.Dependency;
import cn.moyada.faker.module.loader.ModuleLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@Component
public class MethodInvokeHandler extends AbstractHandler {

    @Autowired
    private ModuleLoader moduleLoader;

    @Override
    public MethodHandle fetchHandleInfo(Dependency dependency, String className, String methodName,
                                        String returnType, Class<?>[] paramClass) {
        Class<?> classType, returnClassType;

        try {
//            classType = ReflectUtil.getClassType(className);
            classType = moduleLoader.getClass(dependency, className);
        } catch (ClassNotFoundException e) {
            throw new InitializeInvokerException("接口类型不存在: " + returnType);
        }

        try {
            returnClassType = moduleLoader.getClass(dependency, returnType);
        } catch (ClassNotFoundException e) {
            throw new InitializeInvokerException("结果类型不存在: " + returnType);
        }

        MethodHandle methodHandle;
        MethodHandles.Lookup lookup = moduleLoader.getMethodLookup(dependency);
        try {
            // 创建方法信息
            MethodType methodType = MethodType.methodType(returnClassType, paramClass);
            // 查询方法返回方法具柄
            methodHandle = lookup.findVirtual(classType, methodName, methodType);
        } catch (NoSuchMethodException e) {
            throw new InitializeInvokerException("方法不存在: " + methodName);
        }catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new InitializeInvokerException("方法句柄获取失败");
        }
        return methodHandle;
    }
}
