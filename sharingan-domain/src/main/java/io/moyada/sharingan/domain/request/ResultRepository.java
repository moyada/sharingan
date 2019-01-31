package io.moyada.sharingan.domain.request;


import java.util.Collection;
import java.util.List;

/**
 * 调用结果
 * @author xueyikang
 * @since 0.0.1
 */
public interface ResultRepository {

    /**
     * 保存请求结果
     * @param resultDO
     */
    void saveResult(InvokeResult resultDO);

    /**
     * 批量保存请求结果
     * @param resultDOs
     */
    void saveResult(Collection<InvokeResult> resultDOs);

    int countResult(String reportId);

    /**
     * 获取报告结果
     * @param reportId 报告编号
     * @param pageIndex 页数
     * @param pageSize 每页大小
     * @return
     */
    List<InvokeResult> findResult(String reportId, int pageIndex, int pageSize);
}
