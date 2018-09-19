package cn.moyada.sharingan.storage.mysql.dao;


import cn.moyada.sharingan.storage.api.domain.AppDO;

import java.util.List;

public interface AppDAO {

    int save(AppDO appDO);

    List<AppDO> findAll();

    AppDO findById(int appId);

    AppDO findByName(String appName);

    List<AppDO> findByIds(int[] ids);
}
