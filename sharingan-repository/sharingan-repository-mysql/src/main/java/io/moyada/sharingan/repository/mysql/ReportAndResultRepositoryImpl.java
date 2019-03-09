package io.moyada.sharingan.repository.mysql;

import io.moyada.sharingan.domain.request.InvokeReport;
import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.request.ReportRepository;
import io.moyada.sharingan.domain.request.ResultRepository;
import io.moyada.sharingan.repository.mysql.dao.ReportDAO;
import io.moyada.sharingan.repository.mysql.dao.ResultDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReportAndResultRepositoryImpl implements ResultRepository, ReportRepository {

    private Map<String, Integer> cacheIndex = new ConcurrentHashMap<String, Integer>();

    @Autowired
    private ReportDAO reportDAO;

    @Autowired
    private ResultDAO resultDAO;

    @Override
    public boolean saveReport(InvokeReport report) {
        if (reportDAO.saveReport(report) == 0) {
            return false;
        }
        findReportIndex(report.getReportId());
        return true;
    }

    @Override
    public boolean updateReport(InvokeReport report) {
        return reportDAO.updateReport(report) > 0;
    }

    private Integer findReportIndex(String reportId) {
        Integer index = cacheIndex.get(reportId);
        if (null != index) {
            return index;
        }
        InvokeReport report = findReport(reportId);
        if (null == report) {
            return null;
        }
        index = report.getId();
        cacheIndex.put(reportId, index);
        return index;
    }

    @Override
    public InvokeReport findReport(String reportId) {
        return reportDAO.findReport(reportId);
    }

    @Override
    public void saveResult(InvokeResult result) {
        setIndex(result);
        resultDAO.save(result);
    }

    @Override
    public void saveResult(Collection<InvokeResult> results) {
        for (InvokeResult result : results) {
            setIndex(result);
        }

        resultDAO.saveList(results);
    }

    private void setIndex(InvokeResult result) {
        String reportId = result.getReportId();
        Integer reportIndex = findReportIndex(reportId);
        if (null == reportIndex) {
            return;
        }
        result.setReportId(reportIndex.toString());
    }

    @Override
    public int countResult(String reportId) {
        Integer reportIndex = findReportIndex(reportId);
        if (null == reportIndex) {
            return 0;
        }
        return resultDAO.count(reportIndex);
    }

    @Override
    public List<InvokeResult> findResult(String reportId, int pageIndex, int pageSize) {
        Integer reportIndex = findReportIndex(reportId);
        if (null == reportIndex) {
            return Collections.emptyList();
        }
        if (pageIndex <= 0) {
            return Collections.emptyList();
        }
        int limit = (pageIndex - 1) * pageSize;
        return resultDAO.find(reportIndex, limit, pageSize);
    }
}
