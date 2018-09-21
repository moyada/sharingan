package cn.moyada.sharingan.storage.mysql.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArgsDAO {

    int countByAppAndDomain(@Param("appId") int appId, @Param("domain") String domain);

    List<String> findByAppAndDomain(@Param("appId") int appId, @Param("domain") String domain,
                                    @Param("limit") int limit, @Param("size") int size);
}
