package io.moyada.sharingan.domain.request;

import io.moyada.sharingan.domain.task.ReportData;

import java.sql.Timestamp;

/**
 * 请求报告
 */
public class InvokeReport {

    private Integer id;

    /**
     * 测试序号
     */
    private String reportId;

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

    private InvokeReport() {
    }

    public InvokeReport(String reportId, Integer appId, Integer serviceId, Integer funcId) {
        this.reportId = reportId;
        this.appId = appId;
        this.serviceId = serviceId;
        this.funcId = funcId;
    }

    public void acceptDate(ReportData reportData) {
        this.responseInvoke = reportData.getTotalInvoke();
        this.maxResponseTime = reportData.getMaxResponseTime();
        this.minResponseTime = reportData.getMinResponseTime();
        this.avgResponseTime = reportData.getAvgResponseTime();
        this.successRate = reportData.getSuccessRate();
    }

    public void setTotalInvoke(Integer totalInvoke) {
        this.totalInvoke = totalInvoke;
    }

    public void setDateCreate(Timestamp dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Integer getId() {
        return id;
    }

    public String getReportId() {
        return reportId;
    }

    public Integer getTotalInvoke() {
        return totalInvoke;
    }
    
    public Integer getResponseInvoke() {
        return responseInvoke;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public Integer getMinResponseTime() {
        return minResponseTime;
    }

    public Integer getMaxResponseTime() {
        return maxResponseTime;
    }

    public Integer getAvgResponseTime() {
        return avgResponseTime;
    }

    public Timestamp getDateCreate() {
        return dateCreate;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    private void setReportId(String reportId) {
        this.reportId = reportId;
    }

    private void setAppId(Integer appId) {
        this.appId = appId;
    }

    private void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    private void setFuncId(Integer funcId) {
        this.funcId = funcId;
    }

    private void setResponseInvoke(Integer responseInvoke) {
        this.responseInvoke = responseInvoke;
    }

    private void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    private void setMinResponseTime(Integer minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    private void setMaxResponseTime(Integer maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    private void setAvgResponseTime(Integer avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }
}
