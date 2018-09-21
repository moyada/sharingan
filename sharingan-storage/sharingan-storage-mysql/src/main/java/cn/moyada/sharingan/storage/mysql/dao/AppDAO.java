package cn.moyada.sharingan.storage.mysql.dao;


import cn.moyada.sharingan.storage.api.domain.AppDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppDAO {

    int save(AppDO appDO);

    List<AppDO> findAll();

    AppDO findById(@Param("appId") int appId);

    AppDO findByName(@Param("appName") String appName);

    List<AppDO> findByIds(@Param("ids") int[] ids);
}
