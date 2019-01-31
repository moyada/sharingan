package io.moyada.sharingan.repository.mysql.dao;


import io.moyada.sharingan.domain.request.InvokeResult;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface ResultDAO {

    int save(InvokeResult resultDO);

    int saveList(@Param("list") Collection<InvokeResult> resultDOs);

    List<InvokeResult> find(@Param("reportIndex") int reportIndex,
                            @Param("offset") int offset,
                            @Param("pageSize") int pageSize);

    int count(@Param("reportIndex") int reportIndex);
}
