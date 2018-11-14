package cn.moyada.sharingan.core.queue;


import cn.moyada.sharingan.common.util.ThreadUtil;

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
    private final int length;

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
        this.length = itemDone.length;
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
        E next;
        int nextIndex = index;
        // 轮询数据块
        for(int i = 0; i < length; i++) {
            nextIndex = (nextIndex + i) % length;
            next = sequences[nextIndex].next();
            if(null != next) {
                this.index = nextIndex;
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
            super(producer, (size * 5 / 3) / producer);
        }
    }

    static class UnfairUnlockQueue<U> extends UnlockQueue<U> {
        UnfairUnlockQueue(int producer, int size) {
            super(producer, size);
        }
    }
}