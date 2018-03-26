package cn.moyada.dubbo.faker.core.handle;

import cn.moyada.dubbo.faker.core.exception.InitializeInvokerException;
import cn.moyada.dubbo.faker.core.utils.ReflectUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodInvokeHandle extends AbstractHandle {

    @Override
    public MethodHandle fetchHandleInfo(String className, String methodName, String returnType, Class<?> paramClass[]) {
        Class<?> classType, returnClassType;

        try {
            classType = ReflectUtil.getClassType(className);
        } catch (ClassNotFoundException e) {
            throw new InitializeInvokerException("接口类型不存在: " + returnType);
        }

        try {
            returnClassType = ReflectUtil.getClassType(returnType);
        } catch (ClassNotFoundException e) {
            throw new InitializeInvokerException("结果类型不存在: " + returnType);
        }

        MethodHandle methodHandle;

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            // 创建方法信息
            MethodType methodType = MethodType.methodType(returnClassType, paramClass);
            // 查询方法返回方法具柄
            methodHandle = lookup.findVirtual(classType, methodName, methodType);
        } catch (NoSuchMethodException e) {
            throw new InitializeInvokerException("方法不存在: " + methodName);
        }catch (IllegalAccessException e) {
            throw new InitializeInvokerException("方法具柄获取失败");
        }
        return methodHandle;
    }
}
