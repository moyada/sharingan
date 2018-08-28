package cn.moyada.faker.core.listener;

import cn.moyada.faker.rpc.api.invoke.InvokeCallback;

/**
 * @author xueyikang
 * @create 2018-04-05 15:38
 */
public interface ListenerAction extends InvokeCallback {

    void startListener();

    void waitFinish();
}
