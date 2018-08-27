package cn.moyada.faker.core.invoke;

import cn.moyada.dubbo.faker.core.factory.GroupThreadFactory;
import cn.moyada.dubbo.faker.core.utils.RuntimeUtil;
import cn.moyada.faker.core.common.QuestInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractExecutor implements JobAction {

    protected final ExecutorService executor;

    public AbstractExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    protected static ExecutorService getThreadPool(QuestInfo questInfo, String fakerId) {
        int poolSize = questInfo.getPoolSize();
        int questNum = questInfo.getQuestNum();
        int threadSize = RuntimeUtil.getAllowThreadSize() - poolSize;
        threadSize = questNum > threadSize ? threadSize : questNum;
        return new ThreadPoolExecutor(poolSize, poolSize, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(threadSize),
                new GroupThreadFactory("invoker", fakerId, Thread.MAX_PRIORITY));
    }
}
