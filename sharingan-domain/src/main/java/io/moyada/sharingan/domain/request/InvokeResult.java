package io.moyada.sharingan.domain.request;


import io.moyada.sharingan.infrastructure.constant.HttpStatus;
import io.moyada.sharingan.infrastructure.invoke.data.Result;
import io.moyada.sharingan.infrastructure.util.JsonUtil;
import io.moyada.sharingan.infrastructure.util.ReflectUtil;

import java.sql.Timestamp;

/**
 * 调用结果记录
 *
 * @author xueyikang
 * @create 2017-12-30 06:04
 */
public class InvokeResult {

    private static final String NULL_RESULT = "null";

    private static final int TIMEOUT_MILLISECONDS = 1000;

    private Long id;

    /**
     * 调用编码
     */
    private String reportId;

    /**
     * 实际请求参数
     */
    private String realArgs;

    /**
     * 请求时间
     */
    private Timestamp invokeTime;


    /**
     * 结果码
     */
    private Integer code;

    /**
     * 结果
     */
    private String result;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 耗时
     */
    private Integer responseTime;

    private InvokeResult() {
    }

    public InvokeResult(String reportId, Result result) {
        this.reportId = reportId;
        this.realArgs = result.getArguments();
        this.invokeTime = result.getStartTime();
        this.responseTime = result.getResponseTime();
    }

    public void buildSuccess(Object data, boolean saveResult, String resultParam) {
        if (saveResult) {
            String invokeResult = JsonUtil.toJson(data);
            if (null == invokeResult) {
                this.code = HttpStatus.NOT_FOUND;
                this.result = NULL_RESULT;
                return;
            }

            if (null == resultParam) {
                this.result = invokeResult;
            } else {
                Object param = ReflectUtil.getValue(data, resultParam);
                // 保存结果的单个参数
                if (null == param) {
                    this.code = HttpStatus.NOT_FOUND;
                    this.result = resultParam.concat(": null").intern();
                    return;
                }
                this.result = param.toString();
            }
        }
        this.code = responseTime > TIMEOUT_MILLISECONDS ? HttpStatus.TIME_OUT : HttpStatus.OK;
    }

    public void buildFailure(Result result) {
        this.code = HttpStatus.ERROR;
        this.errorMsg = result.getException();
        this.responseTime = result.getResponseTime();
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Long getId() {
        return id;
    }

    public String getReportId() {
        return reportId;
    }

    public String getRealArgs() {
        return realArgs;
    }

    public Timestamp getInvokeTime() {
        return invokeTime;
    }

    public Integer getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private void setRealArgs(String realArgs) {
        this.realArgs = realArgs;
    }

    private void setInvokeTime(Timestamp invokeTime) {
        this.invokeTime = invokeTime;
    }

    private void setCode(Integer code) {
        this.code = code;
    }

    private void setResult(String result) {
        this.result = result;
    }

    private void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }
}
