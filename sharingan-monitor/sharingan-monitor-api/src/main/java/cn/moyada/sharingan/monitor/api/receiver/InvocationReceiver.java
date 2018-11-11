package cn.moyada.sharingan.monitor.api.receiver;

import cn.moyada.sharingan.monitor.api.entity.Invocation;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface InvocationReceiver<T> {

    T receive(Invocation invocation);
}
