package cn.moyada.sharingan.storage.mysql.dao;

import cn.moyada.sharingan.storage.api.domain.FunctionDO;

import java.util.List;

public interface FunctionDAO {

    int save(FunctionDO functionDO);

    List<FunctionDO> findByService(int serviceId);

    FunctionDO findById(int funcId);
}
