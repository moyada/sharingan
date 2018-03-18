package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.listener.CompletedListener;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.SuspendableCallable;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class FiberInvoke extends AbstractInvoke implements AutoCloseable {

    private final FiberExecutorScheduler scheduler;

    public FiberInvoke(int poolSize, final CompletedListener completedListener) {
        super(Executors.newFixedThreadPool(poolSize), completedListener);
        this.scheduler = new FiberExecutorScheduler("fiber", super.excutor);
    }

    @Suspendable
    @Override
    public void invoke(MethodHandle handle, Object service, Object[] argsValue, String realParam) {
        super.count.increment();
        Timestamp invokeTime = Timestamp.from(Instant.now());

        Fiber<FutureResult> fiber = this.scheduler
                .newFiber((SuspendableCallable<FutureResult>) () -> {
                    FutureResult result;
                    long start = System.nanoTime();
                    try {
                        result = FutureResult.success(super.execute(handle, service, argsValue));
                    } catch (Throwable e) {
                        result = FutureResult.failed(e.getMessage());
                    }
                    result.setSpend((System.nanoTime() - start) / 1000_000);
                    return result;
                })
                .start();

        for (;;) {
            if(fiber.isDone()) {
                try {
                    FutureResult result = fiber.get();
                    super.callback(new InvokeFuture(result, invokeTime, realParam));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                super.count.decrement();
                break;
            }
        }
    }

    @Override
    public void close() {
        this.destroy();
    }
}
