package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncInvoke extends AbstractInvoke implements AutoCloseable {

    private final ExecutorService excutor;
    private final Queue<InvokeFuture> queue;

    public AsyncInvoke(int poolSize, final Queue<InvokeFuture> queue) {
        Double size = poolSize * 1.3;
        this.excutor = Executors.newFixedThreadPool(size.intValue());
        this.queue = queue;
    }

    @Override
    public void invoke(MethodHandle handle, Object service, Object[] argsValue, String realParam) {
        Timestamp invokeTime = Timestamp.from(Instant.now());
        long start = System.nanoTime();

        CompletableFuture.supplyAsync(() -> {
            try {
                return FutureResult.success(super.execute(handle, service, argsValue));
            } catch (Throwable e) {
                return FutureResult.failed(e.getMessage());
            }
        }, this.excutor)
        .whenComplete((result, ex) ->
                {
                    result.setSpend((System.nanoTime() - start) / 1000);
                    queue.offer(new InvokeFuture(result, invokeTime, realParam));
                }
        );
    }

    public void destroy() {
        this.excutor.shutdown();
    }

    @Override
    public void close() {
        this.destroy();
    }
}
