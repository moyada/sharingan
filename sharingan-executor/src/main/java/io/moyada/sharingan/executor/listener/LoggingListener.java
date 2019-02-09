package io.moyada.sharingan.executor.listener;


import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.task.RecordHandler;
import io.moyada.sharingan.domain.task.ReportData;
import io.moyada.sharingan.infrastructure.invoke.InvokeReceiver;
import io.moyada.sharingan.infrastructure.invoke.data.Result;

import java.util.Queue;
import java.util.function.Consumer;

/**
 * 记录结果监听器
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class LoggingListener extends AbstractListener<InvokeResult> implements InvokeReceiver {

    private final Consumer<InvokeResult> resultConsumer;

    public LoggingListener(RecordHandler<InvokeResult> recordHandler, ReportData listenerReportData,
                           Queue<Result> queue, int totalCount,
                           Consumer<InvokeResult> resultConsumer) {
        super(recordHandler, listenerReportData, queue, totalCount);
        this.resultConsumer = resultConsumer;
    }

    @Override
    public void execution() {
        while (true) {
            InvokeResult resultDO = pickUp();
            if (null == resultDO) {
                continue;
            }

            resultConsumer.accept(resultDO);
            if (release(1)) {
                break;
            }
        }

        notifyWait();
    }
}
