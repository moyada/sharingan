package io.moyada.sharingan.service.execute;

import io.moyada.sharingan.domain.request.InvokeReport;
import io.moyada.sharingan.domain.request.ReportRepository;
import io.moyada.sharingan.domain.task.ReportData;
import io.moyada.sharingan.infrastructure.util.TimeUtil;
import io.moyada.sharingan.infrastructure.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class ReportService {

    private ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public String newReport(int appId, int serviceId, int funcId) {
        String id = UUIDUtil.getUUID();
        InvokeReport invokeReport = new InvokeReport(id, appId, serviceId, funcId);
        reportRepository.saveReport(invokeReport);
        return id;
    }

    public boolean buildReport(String reportId, int totalInvoke, ReportData reportData) {
        InvokeReport report = reportRepository.findReport(reportId);
        if (null == report) {
            return false;
        }
        reportData.calculation();
        report.setTotalInvoke(totalInvoke);
        report.acceptDate(reportData);
        report.setDateCreate(TimeUtil.nowTimestamp());
        return reportRepository.updateReport(report);
    }
}
