package io.moyada.sharingan.application;

import io.moyada.sharingan.application.data.PageData;
import io.moyada.sharingan.domain.request.InvokeReport;
import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.request.ReportRepository;
import io.moyada.sharingan.domain.request.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class ResultService {

    private ReportRepository reportRepository;
    private ResultRepository resultRepository;


    @Autowired
    public ResultService(ReportRepository reportRepository, ResultRepository resultRepository) {
        this.reportRepository = reportRepository;
        this.resultRepository = resultRepository;
    }

    public InvokeReport getReport(String reportId) {
        return reportRepository.findReport(reportId);
    }

    public PageData<InvokeResult> getPage(String reportId, int pageIndex, int pageSize) {
        int total = resultRepository.countResult(reportId);
        if (0 == total) {
            return PageData.nil();
        }

        List<InvokeResult> data = resultRepository.findResult(reportId, pageIndex, pageSize);
        return new PageData<>(pageIndex, pageSize, total, data);
    }
}
