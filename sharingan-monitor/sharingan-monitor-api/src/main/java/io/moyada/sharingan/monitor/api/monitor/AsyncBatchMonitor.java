package io.moyada.sharingan.monitor.api.monitor;

import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.util.ThreadUtil;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.api.entity.Invocation;
import io.moyada.sharingan.monitor.api.handler.InvocationConverter;
import io.moyada.sharingan.monitor.api.handler.MultiConsumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 异步处理监视器
 * @author xueyikang
 * @since 1.0
 **/
public class AsyncBatchMonitor<E> extends AbstractAsyncMonitor implements Monitor {

    private final InvocationConverter<E> converter;
    private final MultiConsumer<E> consumer;

    // 持久化数据等待队列
    private final Queue<Collection<E>> waitSet;

    // 临时队列大小
    private final int size;

    // 持久化数据临时队列
    private Collection<E> current;

    // 持久化线程
    private final Thread jobThread;

    // 持久化线程状态
    private final AtomicBoolean sleep;

    public AsyncBatchMonitor(MonitorConfig monitorConfig,
                             InvocationConverter<E> converter,
                             MultiConsumer<E> consumer) {
        super(monitorConfig);
        if (null == converter) {
            throw new NullPointerException("converter can not be null");
        }
        if (null == consumer) {
            throw new NullPointerException("consumer can not be null");
        }

        this.converter = converter;
        this.consumer = consumer;

        this.waitSet = new LinkedBlockingQueue<>();
        this.size = monitorConfig.getThresholdSize();
        this.current = new ArrayList<>(size);

        this.sleep = new AtomicBoolean(false);
        this.jobThread = new AsyncBatchConsumerThread();
        this.jobThread.start();

        int time = monitorConfig.getIntervalTime() * 30;
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (current.isEmpty()) {
                    return;
                }
                if (!setWakeUp()) {
                    return;
                }
                setNewJob();
                notifyJob();
            }
        }, time, time, TimeUnit.MILLISECONDS);

        ThreadUtil.addShutdownHook(new Runnable() {
            @Override
            public void run() {
                jobThread.interrupt();
                if (current.isEmpty()) {
                    return;
                }
                consumer.consumer(current);
            }
        });
    }

    @Override
    protected void consumer(Invocation invocation) {
        E item = converter.convert(invocation);
        if (null == item) {
            return;
        }
        current.add(item);
        if (current.size() > size) {
            setNewJob();
            tryNotifyJob();
        }
    }

    private void setNewJob() {
        waitSet.add(current);
        current = new ArrayList<>(size);
    }

    private boolean setWakeUp() {
        return sleep.compareAndSet(true, false);
    }

    private boolean setSleep() {
        return sleep.compareAndSet(false, true);
    }

    private void tryNotifyJob() {
        if (setWakeUp()) {
            notifyJob();
        }
    }

    private void notifyJob() {
        LockSupport.unpark(jobThread);
    }

    /**
     * 持久化数据处理线程
     */
    private class AsyncBatchConsumerThread extends Thread {

        AsyncBatchConsumerThread() {
            this.setName("Sharingan-Consumer-Thread");
            this.setPriority(Thread.NORM_PRIORITY - 1);
            this.setDaemon(true);
        }

        @Override
        public void run() {
            Collection<E> data;
            for (;;) {
                data = waitSet.poll();

                if (null == data) {
                    if (!setSleep()) {
                        continue;
                    }
                    LockSupport.park(this);
                } else {
                    consumer.consumer(data);
                }

                if (this.isInterrupted()) {
                    break;
                }
            }
        }
    }
}
