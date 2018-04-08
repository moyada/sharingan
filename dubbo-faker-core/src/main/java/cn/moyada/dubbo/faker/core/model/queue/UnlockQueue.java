package cn.moyada.dubbo.faker.core.model.queue;

import cn.moyada.dubbo.faker.core.utils.ThreadUtil;


/**
 * 单消费者无锁队列
 * @author xueyikang
 * @create 2018-04-03 16:14
 */
public class UnlockQueue<E> extends AbstractQueue<E> {

    private final Sequence<E>[] sequences;

    private volatile boolean[] itemDone;

    // 上次获取元素的分组下标
    private int index;

    @SuppressWarnings("unchecked")
    private UnlockQueue(int producer, int size) {
        super(size);
        this.sequences = new Sequence[producer];
        this.itemDone = new boolean[producer];
        for(int index = 0; index < producer; index++) {
            this.sequences[index] = new Sequence<>(size);
            this.itemDone[index] = false;
        }
        this.index = 0;
    }

    @Override
    public void offer(E value) {
        this.offer(ThreadUtil.getInnerGroupId(), value);
    }

    public void offer(int producerIndex, E value) {
        sequences[producerIndex].insert(value);
    }

    @Override
    public E poll() {
        // 检查上次分组
        E next = sequences[index].next();
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
        done();
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