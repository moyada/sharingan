package io.moyada.sharingan.domain.task;

public interface ReportData {

    void record(boolean success, int responseTime);

    void calculation();

    Integer getTotalInvoke();

    Integer getErrorInvoke();

    Double getSuccessRate();

    Integer getMinResponseTime();

    Integer getMaxResponseTime();

    Integer getAvgResponseTime();
}
