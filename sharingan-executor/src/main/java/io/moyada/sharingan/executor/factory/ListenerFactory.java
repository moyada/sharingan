package io.moyada.sharingan.executor.factory;


import io.moyada.sharingan.domain.request.ResultRepository;
import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.task.ListenerAction;
import io.moyada.sharingan.domain.task.RecordHandler;
import io.moyada.sharingan.domain.task.ReportData;
import io.moyada.sharingan.executor.listener.BatchLoggingListener;
import io.moyada.sharingan.executor.listener.LoggingListener;
import io.moyada.sharingan.executor.listener.ListenerReport;
import io.moyada.sharingan.infrastructure.ContextFactory;
import io.moyada.sharingan.infrastructure.invoke.data.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ListenerFactory {

    @Autowired
    private ContextFactory contextFactory;

    private static ReportData buildReportAction(int quest) {
        return new ListenerReport(quest);
    }

    private ResultRepository getInvocationRepository() {
        String className = System.getProperty(ResultRepository.class.getName());
        return contextFactory.getBean(ResultRepository.class, className);
    }

    /**
     * 创建监听器
     *
     */
    public ListenerAction buildListener(RecordHandler<InvokeResult> recordHandler, int quest) {
        Queue<Result> queue = new ConcurrentLinkedQueue<>();

        return new LoggingListener(recordHandler, buildReportAction(quest),
                queue, quest, getInvocationRepository()::saveResult);
    }

    /**
     * 创建批量操作监听器
     * @param quest
     * @return
     */
    public ListenerAction buildBatchListener(RecordHandler<InvokeResult> recordHandler, int quest) {
        // AbstractQueue<Result> queue = UnlockQueue.build(questInfo.getPoolSize(), questInfo.getQuestNum());
        Queue<Result> queue = new ConcurrentLinkedQueue<>();

        return new BatchLoggingListener(recordHandler, buildReportAction(quest),
                queue, quest, getInvocationRepository()::saveResult);
    }
}
