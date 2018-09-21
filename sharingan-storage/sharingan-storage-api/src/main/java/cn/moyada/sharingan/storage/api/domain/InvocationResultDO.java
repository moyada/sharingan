package cn.moyada.sharingan.storage.api.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 调用结果记录
 *
 * @author xueyikang
 * @create 2017-12-30 06:04
 */
public class InvocationResultDO implements Serializable {

    private static final long serialVersionUID = -4290742947963637423L;

    private Long id;

    /**
     * 调用编码
     */
    private String fakerId;

    /**
     * 实际请求参数
     */
    private String realArgs;

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

    /**
     * 请求时间
     */
    private Timestamp invokeTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFakerId() {
        return fakerId;
    }

    public void setFakerId(String fakerId) {
        this.fakerId = fakerId;
    }

    public String getRealArgs() {
        return realArgs;
    }

    public void setRealArgs(String realArgs) {
        this.realArgs = realArgs;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public Timestamp getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(Timestamp invokeTime) {
        this.invokeTime = invokeTime;
    }
}
