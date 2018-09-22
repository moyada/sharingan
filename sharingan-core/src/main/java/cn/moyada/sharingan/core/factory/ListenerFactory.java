package cn.moyada.sharingan.core.factory;

import cn.moyada.sharingan.core.common.QuestInfo;
import cn.moyada.sharingan.core.handler.InvokeRecordHandler;
import cn.moyada.sharingan.core.listener.AbstractListener;
import cn.moyada.sharingan.core.listener.BatchLoggingListener;
import cn.moyada.sharingan.core.listener.ListenerAction;
import cn.moyada.sharingan.core.listener.LoggingListener;
import cn.moyada.sharingan.rpc.api.invoke.Result;
import cn.moyada.sharingan.storage.api.InvocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ListenerFactory {

    @Autowired
    private InvocationRepository invocationRepository;

    /**
     * 创建监听器
     * @param fakerId
     * @param questInfo
     * @return
     */
    public ListenerAction buildListener(String fakerId, QuestInfo questInfo) {
        InvokeRecordHandler recordHandler = new InvokeRecordHandler(fakerId, questInfo.isSaveResult(), questInfo.getResultParam());

        Queue<Result> queue = new ConcurrentLinkedQueue<>();

        AbstractListener listener = new LoggingListener(recordHandler, queue, questInfo.getQuestNum());
        injectRepository(listener);
        return listener;
    }

    /**
     * 创建批量操作监听器
     * @param fakerId
     * @param questInfo
     * @return
     */
    public ListenerAction buildBatchListener(String fakerId, QuestInfo questInfo) {
        InvokeRecordHandler recordHandler = new InvokeRecordHandler(fakerId, questInfo.isSaveResult(), questInfo.getResultParam());

        // AbstractQueue<Result> queue = UnlockQueue.build(questInfo.getPoolSize(), questInfo.getQuestNum());
        Queue<Result> queue = new ConcurrentLinkedQueue<>();

        AbstractListener listener = new BatchLoggingListener(recordHandler, queue, questInfo.getQuestNum());
        injectRepository(listener);
        return listener;
    }

    /**
     * 注入持久层
     * @param listener
     */
    private void injectRepository(AbstractListener listener) {
        listener.setInvocationRepository(invocationRepository);
    }
}
