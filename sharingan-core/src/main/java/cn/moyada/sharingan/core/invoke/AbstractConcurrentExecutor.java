package cn.moyada.sharingan.core.invoke;

import cn.moyada.sharingan.common.util.RuntimeUtil;
import cn.moyada.sharingan.core.factory.GroupThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多线程调用器
 */
public abstract class AbstractConcurrentExecutor implements JobExecutor {

    final ExecutorService executor;

    public AbstractConcurrentExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    protected static ExecutorService getThreadPool(String fakerId, int poolSize, int questNum) {
        int threadSize = RuntimeUtil.getAllowThreadSize() - poolSize;
        threadSize = questNum > threadSize ? threadSize : questNum;

        return new ThreadPoolExecutor(poolSize, poolSize, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(threadSize),
                new GroupThreadFactory("invoker", fakerId, Thread.MAX_PRIORITY));
    }
}
