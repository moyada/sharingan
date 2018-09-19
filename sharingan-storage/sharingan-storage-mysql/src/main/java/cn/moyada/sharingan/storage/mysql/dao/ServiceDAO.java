package cn.moyada.sharingan.storage.mysql.dao;

import cn.moyada.sharingan.storage.api.domain.ServiceDO;

import java.util.List;

public interface ServiceDAO {

    int save(ServiceDO serviceDO);

    List<ServiceDO> findByApp(int appId);

    ServiceDO findById(int serviceId);
}
