package io.moyada.sharingan.application;


import io.moyada.sharingan.application.command.CreateReportCommand;
import io.moyada.sharingan.domain.expression.ParamProvider;
import io.moyada.sharingan.domain.metadada.*;
import io.moyada.sharingan.domain.request.QuestInfo;
import io.moyada.sharingan.domain.request.ReportId;
import io.moyada.sharingan.domain.task.ReportData;
import io.moyada.sharingan.domain.task.TaskProcessor;
import io.moyada.sharingan.infrastructure.ContextFactory;
import io.moyada.sharingan.infrastructure.invoke.InvokeProxy;
import io.moyada.sharingan.infrastructure.invoke.data.ClassInvocation;
import io.moyada.sharingan.infrastructure.invoke.data.HttpInvocation;
import io.moyada.sharingan.infrastructure.invoke.data.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class InvokeService {

    private ContextFactory contextFactory;

    private ReportService reportService;

    private MetadataService metadataService;

    private ExpressionService expressionService;

    private TaskService taskService;

    @Autowired
    public InvokeService(ContextFactory contextFactory,
                         ReportService reportService,
                         MetadataService metadataService,
                         ExpressionService expressionService,
                         TaskService taskService) {
        this.contextFactory = contextFactory;
        this.reportService = reportService;
        this.metadataService = metadataService;
        this.expressionService = expressionService;
        this.taskService = taskService;
    }

    public Result<String> getInvokeData(QuestInfo questInfo) {
        InvokeData invokeData;
        try {
            invokeData = metadataService.getInvokeData(questInfo);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }

        Result<String> result;
        if (invokeData instanceof HttpData) {
            result = doHttpInvoke(questInfo, (HttpData) invokeData);
        } else if (invokeData instanceof ClassData) {
            result = doDubboInvoke(questInfo, (ClassData) invokeData);
        } else {
            return Result.failed("unknown InvocationMetaDate");
        }

        metadataService.reset();
        return result;
    }

    private Result<String> doDubboInvoke(QuestInfo questInfo, ClassData classData) {
        InvokeProxy invokeProxy = getInvokeProxy(classData.getServiceData());

        ClassInvocation classInvocation = classData.getInvocation();
        try {
            invokeProxy.initialize(classInvocation);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }

        ParamProvider paramProvider = expressionService.getMethodParamProvider(questInfo, classData);

        return doInvoke(questInfo, invokeProxy, paramProvider);
    }

    private Result<String> doHttpInvoke(QuestInfo questInfo, HttpData httpData) {
        InvokeProxy invokeProxy = getInvokeProxy(httpData.getServiceData());

        HttpInvocation httpInvocation = httpData.getInvocation();
        try {
            invokeProxy.initialize(httpInvocation);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }

        ParamProvider paramProvider = expressionService.getHttpParamProvider(questInfo, httpInvocation);

        return doInvoke(questInfo, invokeProxy, paramProvider);
    }

    private Result<String> doInvoke(QuestInfo questInfo, InvokeProxy invokeProxy, ParamProvider paramProvider) {
        ReportId reportId = reportService.newReport(
                new CreateReportCommand(
                        questInfo.getAppId(),
                        questInfo.getServiceId(),
                        questInfo.getFunctionId()
                )
        );

        TaskProcessor taskProcessor = taskService.newTask(questInfo, invokeProxy, paramProvider, reportId);

        ReportData reportData = taskProcessor.start(questInfo.getQuest(), questInfo.getQps());

        reportService.buildReport(reportId, questInfo.getQuest(), reportData);
        return Result.success(reportId.getId());
    }

    private InvokeProxy getInvokeProxy(ServiceData serviceData) {
        return contextFactory.getProtocolInvoke(serviceData.getProtocol().getValue(), InvokeProxy.class);
    }
}
