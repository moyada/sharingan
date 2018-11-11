package cn.moyada.sharingan.monitor.api.processor;

import cn.moyada.sharingan.monitor.api.entity.Invocation;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface InvocationWorker extends Runnable {

    void work(Invocation invocation);
}
