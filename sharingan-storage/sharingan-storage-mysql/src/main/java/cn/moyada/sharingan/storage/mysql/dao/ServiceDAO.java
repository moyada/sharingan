package cn.moyada.sharingan.storage.mysql.dao;

import cn.moyada.sharingan.storage.api.domain.ServiceDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServiceDAO {

    int save(ServiceDO serviceDO);

    List<ServiceDO> findByApp(@Param("appId") int appId);

    ServiceDO findById(@Param("serviceId") int serviceId);
}
