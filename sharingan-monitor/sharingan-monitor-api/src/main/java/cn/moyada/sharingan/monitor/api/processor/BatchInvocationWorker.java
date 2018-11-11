package cn.moyada.sharingan.monitor.api.processor;

import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.monitor.api.entity.Record;
import cn.moyada.sharingan.monitor.api.handler.InvocationHandler;
import cn.moyada.sharingan.monitor.api.receiver.InvocationReceiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class BatchInvocationWorker extends DeamonInvocationWorker {

    private final int intervalTime;

    private final int thresholdSize;

    private final Queue<Invocation> temporaryQueue;

    private final Thread handlerThread;

    private volatile boolean sleep;

    private final Queue<Collection<Record>> waitSet;

    public BatchInvocationWorker(InvocationHandler<Collection<Record>> handler,
                                 InvocationReceiver<Record> receiver,
                                 int intervalTime, int thresholdSize) {

        super(handler, receiver, thresholdSize + thresholdSize / 10);

        this.intervalTime = intervalTime;
        this.thresholdSize = thresholdSize;
        this.temporaryQueue = new ConcurrentLinkedQueue<>();

        this.handlerThread = new AsyncHandleThread();
        this.sleep = true;
        this.waitSet = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void work(Invocation invocation) {
        temporaryQueue.add(invocation);
    }

    @Override
    public void run() {
        handlerThread.start();

        for (;;) {
            doExecute();
            sleep();
        }
    }

    private void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(intervalTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void doExecute() {
        Invocation item;

        while (null != (item = temporaryQueue.poll())) {
            Record data = receiver.receive(item);
            nextQueue.add(data);

            if (nextQueue.size() > thresholdSize) {
                addJob(nextQueue);
                nextQueue = new ArrayList<>(size);
            }
        }
    }

    private void addJob(Collection<Record> data) {
        waitSet.offer(data);
        if (sleep) {
            sleep = false;
            LockSupport.unpark(handlerThread);
        }
    }

    class AsyncHandleThread extends Thread {

        @Override
        public void run() {
            Collection<Record> data;
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

        private void interruptSelf() {
            if (this.isInterrupted()) {
                this.interrupt();
            }
        }
    }
}
