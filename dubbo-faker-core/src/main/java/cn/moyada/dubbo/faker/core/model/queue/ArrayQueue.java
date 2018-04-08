package cn.moyada.dubbo.faker.core.model.queue;

/**
 * 单线程队列
 * @author xueyikang
 * @create 2018-04-08 22:29
 */
public class ArrayQueue<E> extends AbstractQueue<E> {

    private final Node<E>[] values;

    private int readIndex;
    private int insertIndex;

    @SuppressWarnings("unchecked")
    public ArrayQueue(int size) {
        super(size);
        this.values = new Node[size];
        this.insertIndex = 0;
        this.readIndex = 0;
    }

    @Override
    public void offer(E value) {
        if(insertIndex == size) {
            throw new IndexOutOfBoundsException("total size is " + size + ", but insert index is " + insertIndex);
        }
        values[insertIndex] = new Node<>(value);
        insertIndex = insertIndex+1;
    }

    @Override
    public E poll() {
        if(readIndex == size) {
            throw new IndexOutOfBoundsException("total size is " + size + ", but read index is " + readIndex);
        }
        Node<E> value = values[readIndex];
        readIndex = readIndex+1;
        return value.value;
    }
}
