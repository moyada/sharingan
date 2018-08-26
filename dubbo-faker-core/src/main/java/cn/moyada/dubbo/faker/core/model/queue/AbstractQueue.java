package cn.moyada.dubbo.faker.core.model.queue;

/**
 * @author xueyikang
 * @create 2018-04-06 10:51
 */
public abstract class AbstractQueue<E> {

    // 数组长度
    protected final int size;

    protected volatile boolean allDone;

    public AbstractQueue(int size) {
        this.size = size;
        this.allDone = false;
    }

    public abstract void offer(E value);

    public abstract E poll();

    public int size() {
        return size;
    }

    /**
     * 生产者停止生产
     */
    public void done() {
        allDone = true;
    }

    /**
     * 是否生产者都停止
     * @return
     */
    public boolean isDone() {
        return allDone;
    }
}
