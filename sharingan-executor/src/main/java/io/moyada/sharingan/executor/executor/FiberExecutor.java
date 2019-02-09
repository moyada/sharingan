package io.moyada.sharingan.executor.executor;

import co.paralleluniverse.fibers.FiberExecutorScheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * 纤线程调用器
 * @author xueyikang
 * @create 2018-08-28 10:27
 */
public class FiberExecutor extends DefaultExecutor {

    private final FiberExecutorScheduler scheduler;

    public FiberExecutor(int poolSize, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory) {
        super(poolSize, blockingQueue, threadFactory);
        this.scheduler = new FiberExecutorScheduler("fiber", executor);
    }

    @Override
    public void execute(Runnable task) {
        this.scheduler
                .newFiber(() -> task)
                .setPriority(Thread.NORM_PRIORITY + 1)
                .start();

//        for (;;) {
//            if(fiber.isDone()) {
//                try {
//                    FutureResult result = fiber.get();
//                    super.callback(new InvokeFuture(result, invokeTime, Arrays.toString(argsValue)));
//                } catch (ExecutionException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//                super.count.decrement();
//                break;
//            }
//        }
    }
}
