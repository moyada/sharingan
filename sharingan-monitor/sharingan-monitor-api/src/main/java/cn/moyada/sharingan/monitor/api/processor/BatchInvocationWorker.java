package cn.moyada.sharingan.monitor.api.processor;

import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.monitor.api.entity.Record;
import cn.moyada.sharingan.monitor.api.handler.InvocationHandler;
import cn.moyada.sharingan.monitor.api.receiver.InvocationReceiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 异步批量处理器
 * @author xueyikang
 * @since 1.0
 **/
public class BatchInvocationWorker<E> extends AbstractInvocationWorker<Record<E>> {

    private final int intervalTime;

    private final int thresholdSize;

    // 调用信息队列
    private final Queue<Invocation> temporaryQueue;

    // 持久化线程
    private final Thread handlerThread;

    // 持久化线程状态
    private volatile boolean sleep;

    // 持久化数据等待队列
    private final Queue<Collection<Record<E>>> waitSet;

    public BatchInvocationWorker(InvocationHandler<Collection<Record<E>>> handler,
                                 InvocationReceiver<Record<E>> receiver,
                                 int intervalTime, int thresholdSize) {
        super(handler, receiver, thresholdSize / 10);
        // super(handler, receiver, thresholdSize + thresholdSize / 10);

        this.intervalTime = intervalTime;
        this.thresholdSize = thresholdSize;
        this.temporaryQueue = new ConcurrentLinkedQueue<>();

        this.handlerThread = new AsyncHandleThread();
        this.sleep = true;
        this.waitSet = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void submit(Invocation invocation) {
        temporaryQueue.add(invocation);
    }

    @Override
    public void run() {
        // 启动持久化线程
        handlerThread.start();

        // 数据处理任务
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                doExecute();
            }
        }, intervalTime, TimeUnit.MILLISECONDS);

//        for (;;) {
//            doExecute();
//            sleep();
//        }
    }

//    private void sleep() {
//        try {
//            TimeUnit.MILLISECONDS.sleep(intervalTime);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }

    /**
     * 处理调用信息队列数据
     */
    private void doExecute() {
        Invocation item;
        Record<E> data;

        while (null != (item = temporaryQueue.poll())) {
            data = receiver.receive(item);
            nextQueue.add(data);

            // 持久化数据队列达到处理数量
            if (nextQueue.size() > thresholdSize) {
                addJob(nextQueue);
                nextQueue = new ArrayList<>(size);
            }
        }
    }

    /**
     * 增加批量任务
     * @param data
     */
    private void addJob(Collection<Record<E>> data) {
        waitSet.offer(data);
        if (sleep) {
            sleep = false;
            LockSupport.unpark(handlerThread);
        }
    }

    /**
     * 持久化数据处理线程
     */
    class AsyncHandleThread extends Thread {

        @Override
        public void run() {
            Collection<Record<E>> data;
            for (;;) {
                data = waitSet.poll();

                if (null == data) {
                    sleep = true;
                    LockSupport.park(this);
                    interruptSelf();
                } else {
                    handler.handle(data);
                }
            }
        }

        /**
         * 唤醒线程
         */
        private void interruptSelf() {
            if (this.isInterrupted()) {
                this.interrupt();
            }
        }
    }
}
