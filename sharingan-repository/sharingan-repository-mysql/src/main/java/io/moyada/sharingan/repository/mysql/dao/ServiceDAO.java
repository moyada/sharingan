package io.moyada.sharingan.repository.mysql.dao;


import io.moyada.sharingan.domain.metadada.ServiceData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServiceDAO {

    int save(ServiceData serviceData);

    List<ServiceData> findByApp(@Param("appId") int appId);

    ServiceData findById(@Param("serviceId") int serviceId);
}
