package cn.xueyikang.dubbo.faker.core.invoke;

import java.lang.invoke.MethodHandle;

public abstract class AbstractInvoke {

    public abstract void invoke(String fakerId, MethodHandle handle, Object[] argsValue);

    public abstract void destroy();
}
