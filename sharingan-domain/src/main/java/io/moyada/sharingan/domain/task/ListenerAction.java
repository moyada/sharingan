package io.moyada.sharingan.domain.task;


import io.moyada.sharingan.infrastructure.invoke.InvokeReceiver;

/**
 * @author xueyikang
 * @create 2018-04-05 15:38
 */
public interface ListenerAction extends InvokeReceiver {

    /**
     * 处理回调
     */
    void process();

    /**
     * 等待工作完成
     */
    ReportData waitFinish();
}
