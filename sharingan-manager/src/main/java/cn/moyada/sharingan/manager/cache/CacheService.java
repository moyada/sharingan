package cn.moyada.sharingan.manager.cache;

import cn.moyada.sharingan.storage.api.InvocationRepository;
import cn.moyada.sharingan.storage.api.domain.InvocationReportDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Component
public class CacheService {

    @Autowired
    private InvocationRepository invocationRepository;

    private InvocationReportDO lastReport;

    public InvocationReportDO getReport(String fakerId) {
        if (null != lastReport && fakerId.equals(lastReport.getFakerId())) {
            return lastReport;
        }

        InvocationReportDO report = invocationRepository.findReport(fakerId);
        if (null != report) {
            lastReport = report;
        }
        return report;
    }
}
