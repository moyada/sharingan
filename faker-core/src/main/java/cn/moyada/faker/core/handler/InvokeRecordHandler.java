package cn.moyada.faker.core.handler;

import cn.moyada.faker.common.utils.JsonUtil;
import cn.moyada.faker.common.utils.ReflectUtil;
import cn.moyada.faker.manager.domain.LogDO;
import cn.moyada.faker.rpc.api.invoke.Result;

/**
 * @author xueyikang
 * @create 2018-08-27 14:53
 */
public class InvokeRecordHandler implements RecordHandler<LogDO> {

    private static final Integer OK = 200;
    private static final Integer NULL = 404;
    private static final Integer ERROR = 500;
    private static final Integer TIME_OUT = 504;

    private static final String NULL_RESULT = "null";

    private static final int TIMEOUT_MILLISECONDS = 1000;

    private String fakerId;

    private String resultParam;

    private Integer invokeId;

    private boolean saveResult;

    public InvokeRecordHandler(String fakerId, Integer invokeId, boolean saveResult, String resultParam) {
        this.fakerId = fakerId;
        this.resultParam = resultParam;
        this.invokeId = invokeId;
        this.saveResult = saveResult;
    }

    @Override
    public String getFakerId() {
        return fakerId;
    }

    @Override
    public LogDO receive(Result result) {
        LogDO logDO = new LogDO();

        if (result.isSuccess()) {
            buildSuccess(logDO, result);
        } else {
            buildFailure(logDO, result);
        }

        logDO.setFakerId(fakerId);
        logDO.setInvokeId(invokeId);
        logDO.setRealParam(result.getArguments());
        logDO.setInvokeTime(result.getStartTime());

        return logDO;
    }

    private void buildSuccess(LogDO logDO, Result result) {
        long rt = result.getResponseTime();
        logDO.setSpendTime(rt);

        if (saveResult) {
            String invokeResult = JsonUtil.toJson(result.getResult());
            if (null == invokeResult) {
                logDO.setCode(NULL);
                logDO.setResult(NULL_RESULT);
                return;
            }

            if (null == resultParam) {
                logDO.setResult(invokeResult);
            } else {
                Object param = ReflectUtil.getValue(result.getResult(), resultParam);
                // 保存结果的单个参数
                if (null == param) {
                    logDO.setCode(NULL);
                    logDO.setResult(resultParam.concat(": null").intern());
                    return;
                }

                logDO.setResult(param.toString());
            }
        }
        logDO.setCode(rt > TIMEOUT_MILLISECONDS ? TIME_OUT : OK);
    }

    private void buildFailure(LogDO logDO, Result result) {
        logDO.setCode(ERROR);
        logDO.setMessage(result.getException());
        logDO.setSpendTime(result.getResponseTime());
    }
}
