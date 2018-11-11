package cn.moyada.sharingan.monitor.api.processor;

import cn.moyada.sharingan.monitor.api.handler.InvocationHandler;
import cn.moyada.sharingan.monitor.api.receiver.InvocationReceiver;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractInvocationWorker<T, R> implements InvocationWorker {

    protected InvocationHandler<T> handler;
    protected InvocationReceiver<R> receiver;

    public AbstractInvocationWorker(InvocationHandler<T> handler, InvocationReceiver<R> receiver) {
        this.handler = handler;
        this.receiver = receiver;
    }
}
