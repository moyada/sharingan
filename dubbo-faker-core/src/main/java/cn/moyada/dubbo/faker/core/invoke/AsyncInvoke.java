package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.listener.CompletedListener;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class AsyncInvoke extends AbstractInvoke implements AutoCloseable {

    public AsyncInvoke(int poolSize, final CompletedListener completedListener) {
        super(Executors.newFixedThreadPool((int) (poolSize * 1.3)), completedListener);
    }

    @Override
    public void invoke(MethodHandle handle, Object service, Object[] argsValue, String realParam) {
        super.count.increment();
        Timestamp invokeTime = Timestamp.from(Instant.now());

        CompletableFuture.supplyAsync(() -> {
            FutureResult result;
            long start = System.nanoTime();
            try {
                result = FutureResult.success(super.execute(handle, service, argsValue));
            } catch (Throwable e) {
                result = FutureResult.failed(e.getMessage());
            }
            result.setSpend(start);
//            result.setSpend((System.nanoTime() - start) / 1000_000);
            return result;
        }, this.excutor)
//        .exceptionally(ex -> FutureResult.failed(ex.getMessage()))
        .whenComplete((result, ex) ->
                {
                    result.setSpend((System.nanoTime() - result.getSpend()) / 1000_000);
//                    result.setSpend(Duration.between(now, Instant.now()).toMillis());
                    super.callback(new InvokeFuture(result, invokeTime, realParam));
                    super.count.decrement();
                }
        );
    }

    @Override
    public void close() {
        this.destroy();
    }
}
