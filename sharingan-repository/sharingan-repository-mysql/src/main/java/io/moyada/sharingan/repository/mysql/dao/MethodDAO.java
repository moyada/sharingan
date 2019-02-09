package io.moyada.sharingan.repository.mysql.dao;


import io.moyada.sharingan.domain.metadada.MethodData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MethodDAO {

    int save(MethodData methodData);

    List<MethodData> findByService(@Param("serviceId") int serviceId);

    MethodData findById(@Param("funcId") int funcId);
}
