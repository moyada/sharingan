package cn.moyada.faker.common.factory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

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
        Thread thread = new Thread(threadGroup, r);
        thread.setPriority(priority);
        thread.setDaemon(false);
        thread.setName("pool-" + threadGroup.getName() + "-thread-" + order.getAndIncrement());
        return thread;
    }

    public static void main(String[] args) {
        ExecutorService pool = new ThreadPoolExecutor(10, 10, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100), new GroupThreadFactory("haha", "test"));

        for (int index = 0; index < 100; index++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                LockSupport.parkNanos(100_000_000);
            });
        }
    }
}