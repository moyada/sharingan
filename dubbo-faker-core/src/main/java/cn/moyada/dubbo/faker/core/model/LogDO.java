package cn.moyada.dubbo.faker.core.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xueyikang
 * @create 2017-12-30 06:04
 */
public class LogDO implements Serializable {

    private Long id;

    private String fakerId;

    private Integer invokeId;

    private String realParam;

    private Integer code;

    private String result;

    private String message;

    private Long spendTime;

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
