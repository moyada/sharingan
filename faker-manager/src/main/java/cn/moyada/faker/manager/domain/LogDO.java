package cn.moyada.faker.manager.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 调用结果记录
 *
 * @author xueyikang
 * @create 2017-12-30 06:04
 */
public class LogDO implements Serializable {

    private static final long serialVersionUID = -4290742947963637423L;

    private Long id;

    /**
     * 调用编码
     */
    private String fakerId;

    /**
     * 请求方法ID
     */
    private Integer invokeId;

    /**
     * 实际请求参数
     */
    private String realParam;

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
    private String message;

    /**
     * 耗时
     */
    private Long spendTime;

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

    public Integer getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(Integer invokeId) {
        this.invokeId = invokeId;
    }

    public String getRealParam() {
        return realParam;
    }

    public void setRealParam(String realParam) {
        this.realParam = realParam;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Long spendTime) {
        this.spendTime = spendTime;
    }

    public Timestamp getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(Timestamp invokeTime) {
        this.invokeTime = invokeTime;
    }
}
