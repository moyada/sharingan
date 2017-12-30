package cn.xueyikang.dubbo.faker.core.invoke;

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
    public CompletableFuture<Object> invoke(String fakerId, MethodHandle handle, Object[] argsValue) {
        return CompletableFuture.supplyAsync(()->{
            try {
                return handle.invoke(argsValue);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return null;
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
