package cn.moyada.sharingan.storage.mysql.dao;

import cn.moyada.sharingan.storage.api.domain.HttpDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HttpDAO {

    int save(HttpDO httpDO);

    List<HttpDO> findByService(@Param("serviceId") int serviceId);

    HttpDO findById(@Param("methodId") int methodId);
}
