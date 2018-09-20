package cn.moyada.sharingan.core.handler;

import cn.moyada.sharingan.common.utils.JsonUtil;
import cn.moyada.sharingan.common.utils.ReflectUtil;
import cn.moyada.sharingan.rpc.api.invoke.Result;
import cn.moyada.sharingan.storage.api.domain.InvocationResultDO;

/**
 * 回调结果处理器
 * @author xueyikang
 * @create 2018-08-27 14:53
 */
public class InvokeRecordHandler implements RecordHandler<InvocationResultDO> {

    private static final Integer OK = 200;
    private static final Integer NULL = 404;
    private static final Integer ERROR = 500;
    private static final Integer TIME_OUT = 504;

    private static final String NULL_RESULT = "null";

    private static final int TIMEOUT_MILLISECONDS = 1000;

    private String fakerId;

    private String resultParam;

    private boolean saveResult;

    public InvokeRecordHandler(String fakerId, boolean saveResult, String resultParam) {
        this.fakerId = fakerId;
        this.resultParam = resultParam;
        this.saveResult = saveResult;
    }

    @Override
    public String getFakerId() {
        return fakerId;
    }

    @Override
    public InvocationResultDO receive(Result result) {
        InvocationResultDO resultDO = new InvocationResultDO();

        if (result.isSuccess()) {
            buildSuccess(resultDO, result);
        } else {
            buildFailure(resultDO, result);
        }

        resultDO.setFakerId(fakerId);
        resultDO.setRealArgs(result.getArguments());
        resultDO.setInvokeTime(result.getStartTime());

        return resultDO;
    }

    private void buildSuccess(InvocationResultDO resultDO, Result result) {
        long rt = result.getResponseTime();
        resultDO.setResponseTime(rt);

        if (saveResult) {
            String invokeResult = JsonUtil.toJson(result.getResult());
            if (null == invokeResult) {
                resultDO.setCode(NULL);
                resultDO.setResult(NULL_RESULT);
                return;
            }

            if (null == resultParam) {
                resultDO.setResult(invokeResult);
            } else {
                Object param = ReflectUtil.getValue(result.getResult(), resultParam);
                // 保存结果的单个参数
                if (null == param) {
                    resultDO.setCode(NULL);
                    resultDO.setResult(resultParam.concat(": null").intern());
                    return;
                }

                resultDO.setResult(param.toString());
            }
        }
        resultDO.setCode(rt > TIMEOUT_MILLISECONDS ? TIME_OUT : OK);
    }

    private void buildFailure(InvocationResultDO resultDO, Result result) {
        resultDO.setCode(ERROR);
        resultDO.setErrorMsg(result.getException());
        resultDO.setResponseTime(result.getResponseTime());
    }
}