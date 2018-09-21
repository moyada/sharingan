package cn.moyada.sharingan.core.listener;

import cn.moyada.sharingan.rpc.api.invoke.InvokeCallback;

/**
 * @author xueyikang
 * @create 2018-04-05 15:38
 */
public interface ListenerAction extends ReportAction, InvokeCallback {

    /**
     * 处理回调
     */
    void process();

    /**
     * 等待工作完成
     */
    void waitFinish();
}
