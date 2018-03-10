package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.SuspendableCallable;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FiberInvoke extends AbstractInvoke implements AutoCloseable {

    private final ExecutorService excutor;
    private final FiberExecutorScheduler scheduler;
    private final Queue<InvokeFuture> queue;

    public FiberInvoke(int poolSize, final Queue<InvokeFuture> queue) {
        this.excutor = Executors.newFixedThreadPool(poolSize);
        this.scheduler = new FiberExecutorScheduler("fiber", this.excutor);
        this.queue = queue;
    }

    @Suspendable
    @Override
    public void invoke(MethodHandle handle, Object service, Object[] argsValue, String realParam) {
        Timestamp invokeTime = Timestamp.from(Instant.now());
        long start = System.nanoTime();

        Fiber<FutureResult> fiber = this.scheduler
                .newFiber((SuspendableCallable<FutureResult>) () -> {
                    try {
                        return FutureResult.success(super.execute(handle, service, argsValue));
                    } catch (Throwable e) {
                        return FutureResult.failed(e);
                    }
                })
                .start();

        for (;;) {
            if(fiber.isDone()) {
                try {
                    FutureResult result = fiber.get();
                    result.setSpend((System.nanoTime() - start) / 1000);
                    queue.offer(new InvokeFuture(result, invokeTime, realParam));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void destroy() {
        this.excutor.shutdown();
    }

    @Override
    public void close() {
        this.destroy();
    }
}
