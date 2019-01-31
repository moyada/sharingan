package io.moyada.sharingan.application;


import io.moyada.sharingan.domain.expression.ParamProvider;
import io.moyada.sharingan.domain.metadada.ClassData;
import io.moyada.sharingan.domain.metadada.InvokeData;
import io.moyada.sharingan.domain.request.QuestInfo;
import io.moyada.sharingan.domain.task.ReportData;
import io.moyada.sharingan.domain.task.TaskProcessor;
import io.moyada.sharingan.infrastructure.ContextFactory;
import io.moyada.sharingan.infrastructure.invoke.InvokeProxy;
import io.moyada.sharingan.infrastructure.invoke.data.ClassInvocation;
import io.moyada.sharingan.infrastructure.invoke.data.HttpInvocation;
import io.moyada.sharingan.infrastructure.invoke.data.Result;
import io.moyada.sharingan.service.execute.ReportService;
import io.moyada.sharingan.service.execute.TaskService;
import io.moyada.sharingan.service.invocation.ExpressionService;
import io.moyada.sharingan.service.invocation.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class InvokeService {

    @Autowired
    private ContextFactory contextFactory;

    @Autowired
    private ReportService reportService;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private ExpressionService expressionService;

    @Autowired
    private TaskService taskService;

    public Result<String> getInvokeData(QuestInfo questInfo) {
        InvokeData invokeData = metadataService.getInvokeData(questInfo);

        Result<String> result;
        if (invokeData.getServiceData().isHttp()) {
            result = doHttpInvoke(questInfo, invokeData);
        } else {
            result = doDubboInvoke(questInfo, invokeData);
        }
        metadataService.reset();
        return result;
    }

    private Result<String> doDubboInvoke(QuestInfo questInfo, InvokeData invokeData) {
        InvokeProxy invokeProxy = getInvokeProxy(invokeData);

        ClassData classData = metadataService.getClassData(invokeData);

        ClassInvocation classInvocation = invokeData.getClassInvocation(classData);
        try {
            invokeProxy.initialize(classInvocation);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }

        ParamProvider paramProvider = expressionService.getMethodParamProvider(questInfo, classData);

        return doInvoke(questInfo, invokeProxy, paramProvider);
    }

    private Result<String> doHttpInvoke(QuestInfo questInfo, InvokeData invokeData) {
        InvokeProxy invokeProxy = getInvokeProxy(invokeData);

        HttpInvocation httpInvocation = invokeData.getHttpInvocation();
        try {
            invokeProxy.initialize(httpInvocation);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }

        ParamProvider paramProvider = expressionService.getHttpParamProvider(questInfo, httpInvocation);

        return doInvoke(questInfo, invokeProxy, paramProvider);
    }

    private Result<String> doInvoke(QuestInfo questInfo, InvokeProxy invokeProxy, ParamProvider paramProvider) {
        String reportId = reportService.newReport(questInfo.getAppId(), questInfo.getServiceId(), questInfo.getFunctionId());

        TaskProcessor taskProcessor = taskService.newTask(questInfo, invokeProxy, paramProvider, reportId);

        ReportData reportData = taskProcessor.start(questInfo.getQuest(), questInfo.getQps());

        reportService.buildReport(reportId, questInfo.getQuest(), reportData);
        return Result.success(reportId);
    }

    private InvokeProxy getInvokeProxy(InvokeData invokeData) {
        return contextFactory.getProtocolInvoke(invokeData.getServiceData().getProtocol(), InvokeProxy.class);
    }
}
