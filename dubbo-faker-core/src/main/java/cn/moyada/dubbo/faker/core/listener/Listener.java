package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.model.InvokeFuture;

/**
 * @author xueyikang
 * @create 2018-03-30 10:33
 */
public interface Listener {

    /**
     * 记录
     * @param result
     */
    void record(InvokeFuture result);

    void shutdown();
}
