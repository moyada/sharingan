package cn.moyada.dubbo.faker.core.model.queue;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 循环无锁队列
 * @author xueyikang
 * @create 2018-04-06 10:56
 */
public class SequenceQueue<E> extends AbstractQueue<E> {

    // 是否重头插入
    private boolean insertLoop;
    // 是否重头读取
    private boolean readLoop;

    // 下次插入下标
    private AtomicInteger insertIndex;

    // 下次读取下标
    private AtomicInteger readIndex;

    // 元素数组
    private Object[] nodes;

    @SuppressWarnings("unchecked")
    public SequenceQueue(int size) {
        super(size);
        this.nodes = new Object[size];
        this.insertIndex = new AtomicInteger(0);
        this.readIndex = new AtomicInteger(0);
        this.insertLoop = false;
        this.readLoop = false;
    }

    @Override
    public void offer(E e) {
        int index = insertIndex.getAndIncrement();
        // 写标记位于读标记并且循环中
        if(readIndex.compareAndSet(index, index) && insertLoop) {
            insertIndex.decrementAndGet();
            throw new IndexOutOfBoundsException("Sequence free size is full, total size is " + size
                    + ", insert index is " + index + ", but read index is " + readIndex.intValue());
        }
        nodes[index] = e;
        if(insertIndex.compareAndSet(size - 1, index) && insertIndex.compareAndSet(index, 0)) {
            insertLoop = true;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        // 读标记等于写标记并且循环中
        int index = readIndex.getAndIncrement();
        if(insertIndex.compareAndSet(index, index) && readLoop) {
            readIndex.decrementAndGet();
            return null;
        }
        Object node = nodes[index];
        if(null == node) {
            readIndex.decrementAndGet();
            return null;
        }

        if(readIndex.compareAndSet(size - 1, index) && readIndex.compareAndSet(index, 0)) {
            readLoop = true;
        }
        return (E) node;
    }
}
