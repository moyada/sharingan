package io.moyada.sharingan.repository.mysql.dao;


import io.moyada.sharingan.domain.metadada.HttpData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HttpDAO {

    List<HttpData> findByService(@Param("serviceId") int serviceId);

    HttpData findById(@Param("methodId") int methodId);
}
