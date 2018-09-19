package cn.moyada.sharingan.storage.api;

import cn.moyada.sharingan.storage.api.domain.InvocationReportDO;
import cn.moyada.sharingan.storage.api.domain.InvocationResultDO;

import java.util.List;

public interface InvocationRepository {

    /**
     * 保存请求信息
     * @param reportDO
     */
    void saveReport(InvocationReportDO reportDO);

    /**
     * 保存请求结果
     * @param resultDO
     */
    void saveResult(InvocationResultDO resultDO);

    /**
     * 批量保存请求结果
     * @param resultDOs
     */
    void saveResult(List<InvocationResultDO> resultDOs);

    /**
     * 获取报告总数
     * @param fakerId 报告编号
     * @return
     */
    InvocationReportDO findReport(String fakerId);

    /**
     * 获取报告结果
     * @param fakerId 报告编号
     * @param pageIndex 页数
     * @param pageSize 每页大小
     * @return
     */
    List<InvocationResultDO> findResult(String fakerId, int pageIndex, int pageSize);
}
