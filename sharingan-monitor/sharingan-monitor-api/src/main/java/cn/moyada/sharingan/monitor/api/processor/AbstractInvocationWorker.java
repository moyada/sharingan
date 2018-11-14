package cn.moyada.sharingan.monitor.api.processor;

import cn.moyada.sharingan.monitor.api.handler.InvocationHandler;
import cn.moyada.sharingan.monitor.api.receiver.InvocationReceiver;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 缓冲处理器
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractInvocationWorker<T> implements InvocationWorker {

    // 临时队列大小
    protected final int size;
    // 持久化数据临时队列
    protected Collection<T> nextQueue;

    protected InvocationHandler<Collection<T>> handler;
    protected InvocationReceiver<T> receiver;

    public AbstractInvocationWorker(InvocationHandler<Collection<T>> handler,
                                    InvocationReceiver<T> receiver,
                                    int size) {
        this.handler = handler;
        this.receiver = receiver;
        this.size = size;
        this.nextQueue = new ArrayList<>(size);
    }

    public Collection<T> getData() {
        return nextQueue;
    }

    public InvocationHandler<Collection<T>> getHandler() {
        return handler;
    }
}
