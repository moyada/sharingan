package cn.moyada.dubbo.faker.core.model.queue;

import cn.moyada.dubbo.faker.core.model.padding.Bit4Padding;
import cn.moyada.dubbo.faker.core.model.padding.Bit7Padding;

import java.util.concurrent.atomic.LongAdder;

/**
 * 循环队列
 * @author xueyikang
 * @create 2018-04-03 16:14
 */
class Sequence<T> extends Bit4Padding {

    // 是否重头插入
    private boolean insertLoop;
    // 是否重头读取
    private boolean readLoop;

    // 数组长度
    private final int size;

    // 下次插入下标
    private LongAdder insertIndex;

    // 下次读取下标
    private LongAdder readIndex;

    // 元素数组
    private Node<T>[] nodes;

    @SuppressWarnings("unchecked")
    Sequence(int size) {
        this.nodes = new Node[size];
        this.size = size;
        this.insertIndex = new LongAdder();
        this.readIndex = new LongAdder();
        this.insertLoop = false;
        this.readLoop = false;
    }

    protected void insert(T t) {
        int index = insertIndex.intValue();
        // 写标记位于读标记并且循环中
        if(index == readIndex.intValue() && insertLoop) {
            throw new IndexOutOfBoundsException("Sequence free size is full, total size is " + size
                    + ", insert index is " + index + ", but read index is " + readIndex.intValue());
        }
        nodes[index] = new Node<>(t);
        if(size == index + 1) {
            insertIndex.reset();
            insertLoop = true;
        }
        else {
            insertIndex.increment();
        }
    }

    protected T next() {
        // 读标记等于写标记并且循环中
        int index = readIndex.intValue();
        if(index == insertIndex.intValue() && readLoop) {
            return null;
        }
        Node<T> node = nodes[index];
        if(null == node) {
            return null;
        }

        if(size == index + 1) {
            readIndex.reset();
            readLoop = true;
        }
        else {
            readIndex.increment();
        }
        return node.value;
    }

    /**
     * 数据元素
     * @param <U>
     */
    class Node<U> extends Bit7Padding {

        private volatile U value;

        Node(U value) {
            this.value = value;
        }
    }
}