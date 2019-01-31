package io.moyada.sharingan.repository.mysql.test;


import io.moyada.sharingan.domain.request.InvokeReport;
import io.moyada.sharingan.domain.request.ReportRepository;
import io.moyada.sharingan.domain.task.ReportData;
import io.moyada.sharingan.infrastructure.util.TimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    @Test
    public void saveReportTest() {
        InvokeReport invokeReport = new InvokeReport("test", 1, 1, 1);
        boolean success = reportRepository.saveReport(invokeReport);
        Assertions.assertTrue(success);
    }

    @Test
    public void updateReportTest() {
        InvokeReport invokeReport = new InvokeReport("test", 1, 1, 1);
        invokeReport.setTotalInvoke(3000);
        invokeReport.acceptDate(new ReportDataTest());
        invokeReport.setDateCreate(TimeUtil.nowTimestamp());
        boolean success = reportRepository.updateReport(invokeReport);
        Assertions.assertTrue(success);
    }

    @Test
    public void findReportTest() {
        InvokeReport test = reportRepository.findReport("test");
        Assertions.assertNotNull(test);
    }

    class ReportDataTest implements ReportData {

        @Override
        public void record(boolean success, int responseTime) {
        }

        @Override
        public void calculation() {
        }

        @Override
        public Integer getTotalInvoke() {
            return 200;
        }

        @Override
        public Integer getErrorInvoke() {
            return 5;
        }

        @Override
        public Double getSuccessRate() {
            return 80.5D;
        }

        @Override
        public Integer getMinResponseTime() {
            return 20;
        }

        @Override
        public Integer getMaxResponseTime() {
            return 3000;
        }

        @Override
        public Integer getAvgResponseTime() {
            return 200;
        }
    }
}
