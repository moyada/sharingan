package io.moyada.sharingan.service.execute;

import io.moyada.sharingan.domain.expression.ParamProvider;
import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.request.QuestInfo;
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

    @Autowired
    private HandlerFactory handlerFactory;

    @Autowired
    private ListenerFactory listenerFactory;

    public TaskProcessor newTask(QuestInfo questInfo, InvokeProxy invokeProxy, ParamProvider paramProvider, String reportId) {
        RecordHandler<InvokeResult> recordHandler = handlerFactory.buildHandler(reportId, questInfo);
        ListenerAction listenerAction = listenerFactory.buildBatchListener(recordHandler, questInfo.getQuest());

        TaskExecutor executor = new DefaultExecutor(questInfo.getConcurrent(),
                new LinkedBlockingQueue<>(),
                new GroupThreadFactory("invoker", reportId, Thread.MAX_PRIORITY));

        return new TaskProcessor(invokeProxy, listenerAction, executor, paramProvider);
    }
}
