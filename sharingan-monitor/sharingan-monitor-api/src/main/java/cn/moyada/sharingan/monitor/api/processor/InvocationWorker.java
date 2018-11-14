package cn.moyada.sharingan.monitor.api.processor;

import cn.moyada.sharingan.monitor.api.entity.Invocation;

/**
 * 调用处理器
 * @author xueyikang
 * @since 1.0
 **/
public interface InvocationWorker extends Runnable {

    /**
     * 处理调用信息
     * @param invocation
     */
    void submit(Invocation invocation);
}
