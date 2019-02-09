package io.moyada.sharingan.application;

import io.moyada.sharingan.domain.expression.ParamProvider;
import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.request.QuestInfo;
import io.moyada.sharingan.domain.request.ReportId;
import io.moyada.sharingan.domain.task.ListenerAction;
import io.moyada.sharingan.domain.task.RecordHandler;
import io.moyada.sharingan.domain.task.TaskExecutor;
import io.moyada.sharingan.domain.task.TaskProcessor;
import io.moyada.sharingan.executor.executor.DefaultExecutor;
import io.moyada.sharingan.executor.factory.GroupThreadFactory;
import io.moyada.sharingan.executor.factory.HandlerFactory;
import io.moyada.sharingan.executor.factory.ListenerFactory;
import io.moyada.sharingan.infrastructure.invoke.InvokeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class TaskService {

    private HandlerFactory handlerFactory;

    private ListenerFactory listenerFactory;

    @Autowired
    public TaskService(HandlerFactory handlerFactory, ListenerFactory listenerFactory) {
        this.handlerFactory = handlerFactory;
        this.listenerFactory = listenerFactory;
    }

    public TaskProcessor newTask(QuestInfo questInfo, InvokeProxy invokeProxy, ParamProvider paramProvider, ReportId reportId) {
        String id = reportId.getId();

        RecordHandler<InvokeResult> recordHandler = handlerFactory.buildHandler(id, questInfo);
        ListenerAction listenerAction = listenerFactory.buildBatchListener(recordHandler, questInfo.getQuest());

        TaskExecutor executor = new DefaultExecutor(questInfo.getConcurrent(),
                new LinkedBlockingQueue<>(),
                new GroupThreadFactory("invoker", id, Thread.MAX_PRIORITY));

        return new TaskProcessor(invokeProxy, listenerAction, executor, paramProvider);
    }
}
