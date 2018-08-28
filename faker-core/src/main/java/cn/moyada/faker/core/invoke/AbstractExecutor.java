package cn.moyada.faker.core.invoke;

import cn.moyada.faker.common.utils.RuntimeUtil;
import cn.moyada.faker.core.common.QuestInfo;
import cn.moyada.faker.core.factory.GroupThreadFactory;

import java.util.concurrent.*;

public abstract class AbstractExecutor implements JobAction {

    protected final ExecutorService executor;

    public AbstractExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    protected static ExecutorService getThreadPool(String fakerId, QuestInfo questInfo) {
        int poolSize = questInfo.getPoolSize();
        int questNum = questInfo.getQuestNum();
        int threadSize = RuntimeUtil.getAllowThreadSize() - poolSize;
        threadSize = questNum > threadSize ? threadSize : questNum;
        return new ThreadPoolExecutor(poolSize, poolSize, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(threadSize),
                new GroupThreadFactory("invoker", fakerId, Thread.MAX_PRIORITY));
    }
}
