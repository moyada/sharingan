package io.moyada.sharingan.executor.factory;

import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.request.QuestInfo;
import io.moyada.sharingan.domain.task.RecordHandler;
import io.moyada.sharingan.executor.handler.InvokeRecordHandler;
import org.springframework.stereotype.Component;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Component
public class HandlerFactory {

    public RecordHandler<InvokeResult> buildHandler(String reportId, QuestInfo questInfo) {
        return new InvokeRecordHandler(reportId, questInfo.getSaveResult(), questInfo.getFilterResult());
    }
}
