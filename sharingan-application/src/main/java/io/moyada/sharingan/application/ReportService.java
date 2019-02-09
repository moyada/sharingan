package io.moyada.sharingan.application;

import io.moyada.sharingan.application.command.CreateReportCommand;
import io.moyada.sharingan.domain.request.InvokeReport;
import io.moyada.sharingan.domain.request.ReportId;
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

    public ReportId newReport(CreateReportCommand createReportCommand) {
        ReportId reportId = new ReportId(UUIDUtil.getUUID());
        InvokeReport invokeReport = new InvokeReport(reportId,
                createReportCommand.getAppId(), createReportCommand.getServiceId(), createReportCommand.getFuncId());
        if (!reportRepository.saveReport(invokeReport)) {
            throw new IllegalStateException();
        }
        return reportId;
    }

    public boolean buildReport(ReportId reportId, int totalInvoke, ReportData reportData) {
        InvokeReport report = reportRepository.findReport(reportId.getId());
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
