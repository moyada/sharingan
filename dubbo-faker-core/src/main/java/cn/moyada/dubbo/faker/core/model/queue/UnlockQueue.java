package cn.moyada.dubbo.faker.core.model.queue;

import cn.moyada.dubbo.faker.core.factory.GroupThreadFactory;
import cn.moyada.dubbo.faker.core.utils.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 单消费者无锁队列
 * @author xueyikang
 * @create 2018-04-03 16:14
 */
public abstract class UnlockQueue<T> {

    private final Sequence<T>[] sequences;

    private volatile boolean[] itemDone;

    private volatile boolean allDone;

    // 上次获取元素的分组下标
    private int index;

    @SuppressWarnings("unchecked")
    UnlockQueue(int producer, int size) {
        this.sequences = new Sequence[producer];
        this.itemDone = new boolean[producer];
        for(int index = 0; index < producer; index++) {
            this.sequences[index] = new Sequence<>(size);
            this.itemDone[index] = false;
        }
        this.index = 0;
        this.allDone = false;
    }

    public void offer(int producerIndex, T value) {
        sequences[producerIndex].insert(value);
    }

    public T poll() {
        // 检查上次分组
        T next = sequences[index].next();
        if(null != next) {
            return next;
        }
        // 检查剩余分组
        int nextIndex = index;
        int length = itemDone.length;
        for(int i = 1; i < length; i++) {
            nextIndex = (nextIndex + i) % length;
            next = sequences[nextIndex].next();
            if(null != next) {
                index = nextIndex;
                return next;
            }
        }
        return null;
    }

    /**
     * 标记下标生产者停止生产
     * @param producer
     */
    public void done(int producer) {
        itemDone[producer] = true;
        for (boolean item : itemDone) {
            if(!item)
                return;
        }
        allDone = true;
    }

    /**
     * 生产者停止生产
     */
    public void done() {
        allDone = true;
    }

    /**
     * 全部生产者都停止生产
     * @return
     */
    public boolean isDone() {
        return allDone;
    }

    public static <U> UnlockQueue<U> build(int producer, int size) {
        return build(producer, size, true);
    }

    public static <U> UnlockQueue<U> build(int producer, int size, boolean fair) {
        if(fair)
            return new FairUnlockQueue<>(producer, size);
        else
            return new UnfairUnlockQueue<>(producer, size);
    }

    static class FairUnlockQueue<U> extends UnlockQueue<U> {
        FairUnlockQueue(int producer, int size) {
            super(producer, (size * 3) / (producer * 2));
        }
    }

    static class UnfairUnlockQueue<U> extends UnlockQueue<U> {
        UnfairUnlockQueue(int producer, int size) {
            super(producer, size);
        }
    }
}