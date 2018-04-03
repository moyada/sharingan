package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.listener.AbstractListener;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.thread.PriorityThread;

import java.util.concurrent.CompletableFuture;

public class AsyncInvoker extends AbstractInvoker {

    public AsyncInvoker(MethodProxy proxy, AbstractListener abstractListener, int poolSize) {
        super(proxy, abstractListener, poolSize);
    }

    @Override
    public void invoke(Object[] argsValue) {
//        super.count.increment();
//        Timestamp invokeTime = Timestamp.from(Instant.now());

        CompletableFuture.runAsync(new PriorityThread(() -> execute(argsValue)), excutor);

//        CompletableFuture.supplyAsync(() ->
//                {
//                    FutureResult result;
//                    // 开始时间
//                    long start = System.nanoTime();
//
//                    try {
//                        result = FutureResult.success(super.execute(argsValue));
//                    } catch (Throwable e) {
//                        result = FutureResult.failed(e.getMessage());
//                    }
//
//                    result.setSpend(start);
//                    return result;
//                }
//
//        , this.excutor)
//        .whenComplete((result, ex) ->
//                {
//                    // 完成计算耗时
//                    result.setSpend((System.nanoTime() - result.getSpend()) / 1000_000);
//                    super.callback(new InvokeFuture(result, invokeTime, Arrays.toString(argsValue)));
//                    super.count.decrement();
//                }
//        );
    }
}
