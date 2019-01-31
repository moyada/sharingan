package io.moyada.sharingan.executor.handler;


import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.task.RecordHandler;
import io.moyada.sharingan.infrastructure.invoke.data.Result;

/**
 * 回调结果处理器
 * @author xueyikang
 * @create 2018-08-27 14:53
 */
public class InvokeRecordHandler implements RecordHandler<InvokeResult> {

    private String fakerId;

    private String resultParam;

    private boolean saveResult;

    public InvokeRecordHandler(String fakerId, boolean saveResult, String resultParam) {
        this.fakerId = fakerId;
        this.resultParam = resultParam;
        this.saveResult = saveResult;
    }

    @Override
    public InvokeResult receive(Result result) {
        InvokeResult invokeResult = new InvokeResult(fakerId, result);

        if (result.isSuccess()) {
            invokeResult.buildSuccess(result.getResult(), saveResult, resultParam);
        } else {
            invokeResult.buildFailure(result);
        }
        return invokeResult;
    }
}
