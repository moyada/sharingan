package cn.moyada.dubbo.faker.core.convert;

import cn.moyada.dubbo.faker.core.common.Code;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.LogDO;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ReflectUtil;

/**
 * @author xueyikang
 * @create 2018-03-29 11:55
 */
public class LoggingConvert {

    private String fakerId;

    private String resultParam;

    private Integer invokeId;

    private boolean saveResult;

    public LoggingConvert(String fakerId, Integer invokeId, boolean saveResult, String resultParam) {
        this.fakerId = fakerId;
        this.resultParam = resultParam;
        this.invokeId = invokeId;
        this.saveResult = saveResult;
    }

    public String getFakerId() {
        return fakerId;
    }

    public LogDO convertToLog(InvokeFuture future) {
        LogDO logDO = new LogDO();

        FutureResult result = future.getFuture();
        long spend = result.getSpend();

        if (result.isSuccess()) {
            if (saveResult) {
                // 是否保存结果
                if (null == resultParam) {
                    String invokeResult = JsonUtil.toJson(result.getResult());
                    if (null == invokeResult) {
                        logDO.setCode(Code.NULL);
                        logDO.setResult("null");
                    }
                    else {
                        logDO.setCode(spend > 1000 ? Code.TIME_OUT : Code.OK);
                        logDO.setResult(invokeResult);
                    }
                }
                else {
                    // 保存结果的单个参数
                    Object param = ReflectUtil.getValue(result.getResult(), resultParam);
                    if (null == param) {
                        logDO.setCode(Code.NULL);
                        logDO.setResult(resultParam + ": null");
                    }
                    else {
                        logDO.setCode(spend > 1000 ? Code.TIME_OUT : Code.OK);
                        logDO.setResult(param.toString());
                    }
                }
            }
            else {
                logDO.setCode(spend > 1000 ? Code.TIME_OUT : Code.OK);
            }
        }
        else {
            logDO.setCode(Code.ERROR);
            logDO.setMessage(result.getResult().toString());
        }

        logDO.setFakerId(fakerId);
        logDO.setInvokeId(invokeId);
        logDO.setSpendTime(spend);
        logDO.setInvokeTime(future.getInvokeTime());
        logDO.setRealParam(future.getRealParam());

        return logDO;
    }
}