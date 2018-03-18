package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.model.InvokeFuture;

/**
 * @author xueyikang
 * @create 2018-03-18 17:12
 */
public interface CompletedListener {

    void record(InvokeFuture result);

    void shutdownDelay();
}
