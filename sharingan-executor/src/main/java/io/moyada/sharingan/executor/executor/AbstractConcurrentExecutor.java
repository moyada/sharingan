package io.moyada.sharingan.executor.executor;


import io.moyada.sharingan.domain.task.TaskExecutor;

import java.util.concurrent.*;

/**
 * 多线程调用器
 */
public abstract class AbstractConcurrentExecutor implements TaskExecutor {

    final ExecutorService executor;

    public AbstractConcurrentExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    protected static ExecutorService getThreadPool(int poolSize, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(poolSize, poolSize, 10L, TimeUnit.SECONDS,
                blockingQueue, null == threadFactory ? Executors.defaultThreadFactory() : threadFactory);
    }
}
