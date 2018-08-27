package cn.moyada.faker.core.queue;

import cn.moyada.dubbo.faker.core.model.padding.Bit4Padding;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 单线程循环队列
 * @author xueyikang
 * @create 2018-04-03 16:14
 */
class Sequence<E> extends Bit4Padding {

    // 是否重头插入
    private boolean insertLoop;
    // 是否重头读取
    private boolean readLoop;

    // 数组长度
    private final int size;

    // 下次插入下标
    private AtomicInteger insertIndex;

    // 下次读取下标
    private AtomicInteger readIndex;

    // 元素数组
    private Object[] nodes;

    @SuppressWarnings("unchecked")
    Sequence(int size) {
        this.nodes = new Object[size];
        this.size = size;
        this.insertIndex = new AtomicInteger(0);
        this.readIndex = new AtomicInteger(0);
        this.insertLoop = false;
        this.readLoop = false;
    }

    protected void insert(E e) {
        int index = insertIndex.intValue();
        // 写标记位于读标记并且循环中
        if(index == readIndex.intValue() && insertLoop) {
            throw new IndexOutOfBoundsException("Sequence free size is full, total size is " + size
                    + ", insert index is " + index + ", but read index is " + readIndex.intValue());
        }
        nodes[index] = e;
        if(size == index + 1) {
            insertIndex.set(0);
            insertLoop = true;
        }
        else {
            insertIndex.incrementAndGet();
        }
    }

    @SuppressWarnings("unchecked")
    protected E next() {
        // 读标记等于写标记并且循环中
        int index = readIndex.intValue();
        if(index == insertIndex.intValue() && readLoop) {
            return null;
        }
        Object node = nodes[index];
        if(null == node) {
            return null;
        }

        if(size == index + 1) {
            readIndex.set(0);
            readLoop = true;
        }
        else {
            readIndex.incrementAndGet();
        }
        return (E) node;
    }
}