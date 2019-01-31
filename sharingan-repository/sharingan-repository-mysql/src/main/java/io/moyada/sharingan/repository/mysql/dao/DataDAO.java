package io.moyada.sharingan.repository.mysql.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataDAO {

    int countByAppAndDomain(@Param("appId") int appId, @Param("domain") String domain);

    List<String> findByAppAndDomain(@Param("appId") int appId, @Param("domain") String domain,
                                    @Param("limit") int limit, @Param("size") int size);
}
