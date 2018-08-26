package cn.moyada.faker.common.model.queue;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子操作无锁队列
 * @author xueyikang
 * @create 2018-04-06 10:36
 */
public class AtomicQueue<E> extends AbstractQueue<E> {

    private final AtomicInteger insertIndex;
    private final AtomicInteger readIndex;

    private final Object[] values;

    @SuppressWarnings("unchecked")
    public AtomicQueue(int size) {
        super(size);
        this.values = new Object[size];
        this.insertIndex = new AtomicInteger(0);
        this.readIndex = new AtomicInteger(0);
    }

    @Override
    public void offer(E value) {
        int i = insertIndex.getAndIncrement();

        if(i == size) {
            throw new IndexOutOfBoundsException("total size is " + size + ", but insert index is " + i);
        }
        values[i] = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        int i = readIndex.getAndIncrement();
        if(insertIndex.compareAndSet(i, i)) {
            readIndex.decrementAndGet();
            return null;
        }

        if(i == size) {
            done();
            return null;
        }
        Object node = values[i];
        if(null == node) {
            readIndex.decrementAndGet();
            return null;
        }
        return (E) node;
    }
}
