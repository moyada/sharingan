package cn.moyada.sharingan.core.invoke;

import co.paralleluniverse.fibers.FiberExecutorScheduler;

/**
 * @author xueyikang
 * @create 2018-08-28 10:27
 */
public class FiberExecutor extends DefaultExecutor implements JobAction {

    private final FiberExecutorScheduler scheduler;

    public FiberExecutor(String fakerId, int poolSize, int questNum) {
        super(fakerId, poolSize, questNum);
        this.scheduler = new FiberExecutorScheduler("fiber", executor);
    }

    @Override
    public void run(Runnable task) {
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
