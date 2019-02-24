package io.moyada.sharingan.repository.mysql.dao;


import io.moyada.sharingan.domain.metadada.AppData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppDAO {

    List<AppData> findAll();

    AppData findById(@Param("appId") int appId);

    AppData findByName(@Param("appName") String appName);

    List<AppData> findByIds(@Param("ids") int[] ids);
}
