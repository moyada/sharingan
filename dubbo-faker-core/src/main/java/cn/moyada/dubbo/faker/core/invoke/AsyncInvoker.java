package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.listener.CompletedListener;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class AsyncInvoker extends AbstractInvoker {

    public AsyncInvoker(MethodHandle handle, Object service,
                        CompletedListener completedListener, int poolSize) {
        super(handle, service, completedListener, poolSize);
    }

    @Override
    public void invoke(Object[] argsValue) {
        super.count.increment();
        Timestamp invokeTime = Timestamp.from(Instant.now());

        CompletableFuture.supplyAsync(() -> {
            FutureResult result;
            // 开始时间
            long start = System.nanoTime();
            try {
                result = FutureResult.success(super.execute(argsValue));
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
                    // 完成计算耗时
                    result.setSpend((System.nanoTime() - result.getSpend()) / 1000_000);
//                    result.setSpend(Duration.between(now, Instant.now()).toMillis());
                    super.callback(new InvokeFuture(result, invokeTime, Arrays.toString(argsValue)));
                    super.count.decrement();
                }
        );
    }
}
