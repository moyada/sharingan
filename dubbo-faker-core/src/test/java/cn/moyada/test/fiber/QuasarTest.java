package cn.moyada.test.fiber;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.strands.SuspendableCallable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xueyikang
 * @create 2018-02-04 00:13
 */
public class QuasarTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        FiberExecutorScheduler scheduler = new FiberExecutorScheduler(null, pool);
        Fiber<Object> fiber = scheduler
                .newFiber((SuspendableCallable<Object>) () -> {
                    try {
                        return "ok";
                    } catch (Throwable e) {
                        return e;
                    }
                })
                .start();
        System.out.println(fiber.isTerminated());
        while (true) {
            if (fiber.isDone()) {
                System.out.println(fiber.get());
                break;
            }
        }
        System.out.println("done");
        System.out.println(fiber.isTerminated());
        pool.shutdown();
    }
}
