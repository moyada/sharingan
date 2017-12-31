package cn.xueyikang.dubbo.faker.core.invoke;

import cn.xueyikang.dubbo.faker.core.exception.UnsupportParamNumException;

import java.lang.invoke.MethodHandle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncInvoke extends AbstractInvoke implements AutoCloseable {

    private final ExecutorService excutor;

    public AsyncInvoke(int poolSize) {
        this.excutor = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    public CompletableFuture<Object> invoke(String fakerId, MethodHandle handle, Object service, Object[] argsValue) {
        return CompletableFuture.supplyAsync(()->{
            try {
                if(null == argsValue) {
                    return handle.invoke(service);
                }
                switch (argsValue.length) {
                    case 0:
                        return handle.invoke(service);
                    case 1:
                        return handle.invoke(service, argsValue[0]);
                    case 2:
                        return handle.invoke(service, argsValue[0], argsValue[1]);
                    case 3:
                        return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2]);
                    case 4:
                        return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3]);
                    case 5:
                        return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4]);
                    case 6:
                        return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5]);
                    case 7:
                        return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5], argsValue[6]);
                    default:
                        return new UnsupportParamNumException("[Faker Invoker Error] Param number not yet support.");
                }
            } catch (Throwable e) {
                return e;
            }
        }, this.excutor);
    }

    public void destroy() {
        this.excutor.shutdown();
    }

    @Override
    public void close() {
        this.destroy();
    }
}
