package cn.moyada.sharingan.core.invoke;

import cn.moyada.sharingan.common.utils.RuntimeUtil;
import cn.moyada.sharingan.core.factory.GroupThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractExecutor implements JobAction {

    final ExecutorService executor;

    public AbstractExecutor(ExecutorService executor) {
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
