package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.listener.AbstractListener;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.Suspendable;

import java.lang.invoke.MethodHandle;

public class FiberInvoker extends AbstractInvoker {

    private final FiberExecutorScheduler scheduler;

    public FiberInvoker(MethodHandle handle, Object service,
                        AbstractListener abstractListener, int poolSize) {
        super(handle, service, abstractListener, poolSize);
        this.scheduler = new FiberExecutorScheduler("fiber", super.excutor);
    }

    @Suspendable
    @Override
    public void invoke(Object[] argsValue) {
        super.count.increment();
//        Timestamp invokeTime = Timestamp.from(Instant.now());

        this.scheduler
                .newFiber(() ->  {
                    execute(argsValue);
                    return null;
                })
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
