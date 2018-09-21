package cn.moyada.sharingan.storage.api.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 请求报告
 */
public class InvocationReportDO implements Serializable {

    private static final long serialVersionUID = -9006539234490441270L;

    /**
     * 测试序号
     */
    private String fakerId;

    /**
     * 项目编号
     */
    private Integer appId;

    /**
     * 服务编号
     */
    private Integer serviceId;

    /**
     * 方法编号
     */
    private Integer funcId;

    /**
     * 请求次数
     */
    private Integer totalInvoke;

    /**
     * 响应次数
     */
    private Integer responseInvoke;

    /**
     * 成功比率
     */
    private Double successRate;

    /**
     * 最小耗时
     */
    private Integer minResponseTime;

    /**
     * 最大耗时
     */
    private Integer maxResponseTime;

    /**
     * 平均耗时
     */
    private Integer avgResponseTime;

    /**
     * 开始时间
     */
    private Timestamp dateCreate;

    public String getFakerId() {
        return fakerId;
    }

    public void setFakerId(String fakerId) {
        this.fakerId = fakerId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getFuncId() {
        return funcId;
    }

    public void setFuncId(Integer funcId) {
        this.funcId = funcId;
    }

    public Integer getTotalInvoke() {
        return totalInvoke;
    }

    public void setTotalInvoke(Integer totalInvoke) {
        this.totalInvoke = totalInvoke;
    }

    public Integer getResponseInvoke() {
        return responseInvoke;
    }

    public void setResponseInvoke(Integer responseInvoke) {
        this.responseInvoke = responseInvoke;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public Integer getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(Integer minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public Integer getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Integer maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Integer getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(Integer avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public Timestamp getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Timestamp dateCreate) {
        this.dateCreate = dateCreate;
    }
}
