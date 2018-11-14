package cn.moyada.sharingan.monitor.api.receiver;

import cn.moyada.sharingan.monitor.api.entity.Invocation;

/**
 * 调用数据接收器
 * @author xueyikang
 * @since 1.0
 **/
public interface InvocationReceiver<T> {

    T receive(Invocation invocation);
}
