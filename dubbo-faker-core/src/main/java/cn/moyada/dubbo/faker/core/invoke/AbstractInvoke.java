package cn.moyada.dubbo.faker.core.invoke;

import java.lang.invoke.MethodHandle;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractInvoke {

    public abstract CompletableFuture<Object> invoke(String fakerId, MethodHandle handle, Object service, Object[] argsValue);

    public abstract void destroy();
}
