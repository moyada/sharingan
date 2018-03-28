package cn.moyada.dubbo.faker.core.handler;

import java.lang.invoke.MethodHandle;

/**
 * 代理抽象类
 */
public abstract class AbstractHandler {

    /**
     * 获取代理
     */
    public abstract MethodHandle fetchHandleInfo(String className, String methodName, String returnType, Class<?> paramClass[]);
}
