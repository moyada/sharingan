package io.moyada.sharingan.domain.request;


/**
 * 调用结果
 * @author xueyikang
 * @since 0.0.1
 */
public interface ReportRepository {

    /**
     * 保存请求信息
     * @param reportDO
     */
    boolean saveReport(InvokeReport reportDO);

    boolean updateReport(InvokeReport reportDO);

    /**
     * 获取报告总数
     * @param reportId 报告编号
     * @return
     */
    InvokeReport findReport(String reportId);
}
