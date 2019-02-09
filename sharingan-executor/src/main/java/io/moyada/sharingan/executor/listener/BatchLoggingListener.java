package io.moyada.sharingan.executor.listener;


import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.task.RecordHandler;
import io.moyada.sharingan.domain.task.ReportData;
import io.moyada.sharingan.infrastructure.invoke.InvokeReceiver;
import io.moyada.sharingan.infrastructure.invoke.data.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * 批量保存结果监听器
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class BatchLoggingListener extends AbstractListener<InvokeResult> implements InvokeReceiver {

    private static final int OPT_SIZE = 1000;

    private Consumer<Collection<InvokeResult>> resultConsumer;

    public BatchLoggingListener(RecordHandler<InvokeResult> recordHandler, ReportData listenerReportData,
                                Queue<Result> queue, int totalCount,
                                Consumer<Collection<InvokeResult>> resultConsumer) {
        super(recordHandler, listenerReportData, queue, totalCount);
        this.resultConsumer = resultConsumer;
    }

    @Override
    public void execution() {
        List<InvokeResult> results = new ArrayList<>(OPT_SIZE);
        int size = 0;

        while (true) {
            InvokeResult result = pickUp();
            if (null != result) {
                size++;
                results.add(result);
                if (size != OPT_SIZE) {
                    continue;
                }
            } else if (size == 0) {
                continue;
            }

            resultConsumer.accept(results);
            results.clear();
            if (release(size)) {
                break;
            }
            size = 0;
        }

        notifyWait();
    }
}
