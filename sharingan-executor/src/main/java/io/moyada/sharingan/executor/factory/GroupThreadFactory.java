package io.moyada.sharingan.executor.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分组线程工厂
 * @author xueyikang
 * @create 2018-04-03 15:35
 */
public class GroupThreadFactory implements ThreadFactory {

    protected final ThreadGroup threadGroup;

    private final AtomicInteger order;

    private final int priority;

    public GroupThreadFactory(String poolMark, String groupName) {
        this(poolMark, groupName, Thread.NORM_PRIORITY);
    }

    public GroupThreadFactory(String poolMark, String groupName, int priority) {
        this.threadGroup = new ThreadGroup(poolMark +  "&" + groupName);
        this.threadGroup.setMaxPriority(priority);
        this.priority = priority;
        this.order = new AtomicInteger(0);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r, "pool-" + threadGroup.getName() + "-thread-" + order.getAndIncrement());
        thread.setPriority(priority);
        return thread;
    }
}