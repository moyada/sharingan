package io.moyada.sharingan.monitor.api.monitor;

import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.util.ThreadUtil;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.api.entity.Invocation;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * 异步处理监视器
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractAsyncMonitor implements Monitor {

    // 调用信息队列
    public final Queue<Invocation> data;

    protected final ScheduledExecutorService executorService;

    public AbstractAsyncMonitor(MonitorConfig monitorConfig) {
        data = new ConcurrentLinkedQueue<>();
        executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Sharingan-Monitor-Thread");
                thread.setPriority(Thread.NORM_PRIORITY - 1);
                thread.setDaemon(true);
                return thread;
            }
        });
        executorService.scheduleAtFixedRate(new Worker(), 0, monitorConfig.getIntervalTime(), TimeUnit.MILLISECONDS);

        ThreadUtil.addShutdownHook(new Runnable() {
            @Override
            public void run() {
                executorService.shutdown();
            }
        });
    }

    @Override
    public void listener(Invocation invocation) {
        data.add(invocation);
    }

    abstract protected void consumer(Invocation invocation);

    private class Worker implements Runnable {

        @Override
        public void run() {
            Invocation poll;
            while (null != (poll = data.poll())) {
                consumer(poll);
            }
        }
    }
}
