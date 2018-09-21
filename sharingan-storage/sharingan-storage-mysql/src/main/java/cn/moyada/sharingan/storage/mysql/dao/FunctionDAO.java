package cn.moyada.sharingan.storage.mysql.dao;

import cn.moyada.sharingan.storage.api.domain.FunctionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FunctionDAO {

    int save(FunctionDO functionDO);

    List<FunctionDO> findByService(@Param("serviceId") int serviceId);

    FunctionDO findById(@Param("funcId") int funcId);
}
