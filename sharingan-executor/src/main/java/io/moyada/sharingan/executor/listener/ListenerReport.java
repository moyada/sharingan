package io.moyada.sharingan.executor.listener;


import io.moyada.sharingan.domain.task.ReportData;

/**
 * 监听报告
 */
public class ListenerReport implements ReportData {

    /**
     * 请求次数
     */
    private int totalInvoke;

    /**
     * 错误次数
     */
    private int errorInvoke = 0;

    /**
     * 成功概率
     */
    private double successRate;

    /**
     * 最小耗时
     */
    private int minResponseTime = Integer.MAX_VALUE;

    /**
     * 最大耗时
     */
    private int maxResponseTime = -1;

    /**
     * 平均耗时
     */
    protected int avgResponseTime;

    /**
     * 平均耗时
     */
    protected int totalResponseTime;

    public ListenerReport(int totalInvoke) {
        this.totalInvoke = totalInvoke;
    }

    @Override
    public void record(boolean success, int responseTime) {
        if (!success) {
            errorInvoke++;
        }

        if (responseTime > maxResponseTime) {
            maxResponseTime = responseTime;
        }
        if (responseTime < minResponseTime) {
            minResponseTime = responseTime;
        }

        totalResponseTime += responseTime;
    }

    @Override
    public void calculation() {
        if (errorInvoke > 0) {
            successRate = totalInvoke == errorInvoke ? 1D : totalInvoke * 1.0D / (totalInvoke - errorInvoke);
        }
        avgResponseTime = totalResponseTime / totalInvoke;
    }

    @Override
    public Integer getTotalInvoke() {
        return totalInvoke;
    }

    @Override
    public Integer getErrorInvoke() {
        return errorInvoke;
    }

    @Override
    public Double getSuccessRate() {
        return successRate;
    }

    @Override
    public Integer getMinResponseTime() {
        return minResponseTime;
    }

    @Override
    public Integer getMaxResponseTime() {
        return maxResponseTime;
    }

    @Override
    public Integer getAvgResponseTime() {
        return avgResponseTime;
    }
}
