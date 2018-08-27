package cn.moyada.faker.core.queue;

/**
 * 单线程队列
 * @author xueyikang
 * @create 2018-04-08 22:29
 */
public class ArrayQueue<E> extends AbstractQueue<E> {

    private Object[] values;

    private int readIndex;
    private int insertIndex;

    @SuppressWarnings("unchecked")
    public ArrayQueue(int size) {
        super(size);
        this.values = new Object[size];
        this.insertIndex = 0;
        this.readIndex = 0;
    }

    @Override
    public void offer(E value) {
        if(insertIndex == size) {
            throw new IndexOutOfBoundsException("total size is " + size + ", but insert index is " + insertIndex);
        }
        values[insertIndex] = value;
        insertIndex = insertIndex+1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if(readIndex == size) {
            return null;
        }
        Object node = values[readIndex];
        if(null == node) {
//            done();
            return null;
        }
        readIndex = readIndex+1;
        return (E) node;
    }
}
