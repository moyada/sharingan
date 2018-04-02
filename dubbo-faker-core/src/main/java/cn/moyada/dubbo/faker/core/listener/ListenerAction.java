package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.model.InvokeFuture;

/**
 * 监听器动作
 * @author xueyikang
 * @create 2018-03-30 10:33
 */
public interface ListenerAction {

    /**
     * 记录
     * @param result
     */
    void record(InvokeFuture result);

    void shutdown();
}
