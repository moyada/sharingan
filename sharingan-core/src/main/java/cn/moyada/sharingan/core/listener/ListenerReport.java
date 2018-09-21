package cn.moyada.sharingan.core.listener;

import cn.moyada.sharingan.storage.api.domain.InvocationResultDO;

/**
 * 监听报告
 */
public class ListenerReport implements ReportAction {

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

    public ListenerReport(Integer totalInvoke) {
        this.totalInvoke = totalInvoke;
    }

    protected void record(InvocationResultDO resultDO) {
        if (null != resultDO.getErrorMsg()) {
            errorInvoke++;
        }

        int rt = resultDO.getResponseTime();
        if (rt > maxResponseTime) {
            maxResponseTime = rt;
        }
        if (rt < minResponseTime) {
            minResponseTime = rt;
        }

        totalResponseTime += rt;
    }

    @Override
    public ListenerReport buildReport() {
        this.successRate = totalInvoke * 1.0D / (totalInvoke - errorInvoke);
        this.avgResponseTime = totalResponseTime / totalInvoke;
        return this;
    }

    public Integer getTotalInvoke() {
        return totalInvoke;
    }

    public Integer getErrorInvoke() {
        return errorInvoke;
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

    public Integer getTotalResponseTime() {
        return totalResponseTime;
    }
}
