package cn.moyada.sharingan.storage.mysql.dao;

import cn.moyada.sharingan.storage.api.domain.InvocationReportDO;
import cn.moyada.sharingan.storage.api.domain.InvocationResultDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InvocationDAO {

    int saveReport(InvocationReportDO reportDO);

    int saveResult(InvocationResultDO resultDO);

    int saveBatchResult(@Param("list") List<InvocationResultDO> resultDOs);

    InvocationReportDO findReport(@Param("fakerId") String fakerId);

    List<InvocationResultDO> findResult(@Param("fakerId") String fakerId,
                                              @Param("offset") int offset,
                                              @Param("pageSize") int pageSize);
}
