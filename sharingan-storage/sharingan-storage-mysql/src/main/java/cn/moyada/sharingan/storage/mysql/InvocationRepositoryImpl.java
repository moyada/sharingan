package cn.moyada.sharingan.storage.mysql;

import cn.moyada.sharingan.storage.api.InvocationRepository;
import cn.moyada.sharingan.storage.api.domain.InvocationReportDO;
import cn.moyada.sharingan.storage.api.domain.InvocationResultDO;
import cn.moyada.sharingan.storage.mysql.dao.InvocationDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InvocationRepositoryImpl implements InvocationRepository {

    @Autowired
    private InvocationDAO invocationDAO;

    @Override
    public void saveReport(InvocationReportDO reportDO) {
        invocationDAO.saveReport(reportDO);
    }

    @Override
    public void saveResult(InvocationResultDO resultDO) {
        invocationDAO.saveResult(resultDO);
    }

    @Override
    public void saveResult(List<InvocationResultDO> resultDOs) {
        invocationDAO.saveBatchResult(resultDOs);
    }

    @Override
    public InvocationReportDO findReport(String fakerId) {
        return invocationDAO.findReport(fakerId);
    }

    @Override
    public List<InvocationResultDO> findResult(String fakerId, int pageIndex, int pageSize) {
        int limit = (pageIndex - 1) * pageSize;
        return invocationDAO.findResult(fakerId, limit, pageSize);
    }
}
