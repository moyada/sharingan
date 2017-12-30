package cn.xueyikang.dubbo.faker.core.model;

import java.sql.Timestamp;

/**
 * @author xueyikang
 * @create 2017-12-30 06:04
 */
public class LogDO {

    private String fakerId;

    private Integer code;

    private String message;

    private Long spendTime;

    private Timestamp invokeTime;

    public String getFakerId() {
        return fakerId;
    }

    public void setFakerId(String fakerId) {
        this.fakerId = fakerId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
