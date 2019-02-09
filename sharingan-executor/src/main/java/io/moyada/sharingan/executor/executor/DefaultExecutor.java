package io.moyada.sharingan.executor.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * 默认调用器
 * @author xueyikang
 * @since 0.0.1
 */
public class DefaultExecutor extends AbstractConcurrentExecutor {

    public DefaultExecutor(int poolSize, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory) {
        super(getThreadPool(poolSize, blockingQueue, threadFactory));
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}
