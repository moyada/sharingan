package io.moyada.sharingan.repository.mysql.dao;


import io.moyada.sharingan.domain.request.InvokeReport;
import org.apache.ibatis.annotations.Param;

public interface ReportDAO {

    int saveReport(InvokeReport reportDO);

    int updateReport(InvokeReport reportDO);

    InvokeReport findReport(@Param("reportId") String reportId);
}
