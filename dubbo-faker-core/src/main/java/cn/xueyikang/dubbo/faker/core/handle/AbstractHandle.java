package cn.xueyikang.dubbo.faker.core.handle;

import java.lang.invoke.MethodHandle;

public abstract class AbstractHandle {

    public abstract MethodHandle fetchHandleInfo(String className, String methodName, String returnType, Class<?> paramClass[]);
}
