package cn.moyada.dubbo.faker.core.dao;

import cn.moyada.dubbo.faker.core.model.LogDO;
import cn.moyada.dubbo.faker.core.model.MethodInvokeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FakerDAO {

    void saveInvokeInfo(MethodInvokeDO methodInvokeDO);

    List<MethodInvokeDO> findAll();

    MethodInvokeDO findInvokeInfoById(@Param("id") int id);

    List<String> findParamByType(@Param("appId") int appId, @Param("type") String type);

    void saveLog(LogDO logDO);

    void saveLogList(@Param("list") List<LogDO> logDOs);

    List<MethodInvokeDO> findAllApp();

    List<String> findClassByApp(@Param("appId") int appId);

    List<MethodInvokeDO> findMethodByClass(@Param("className") String className);

    int countMethodByFakerId(@Param("fakerId") String fakerId);

    List<LogDO> findMethodByFakerId(@Param("fakerId") String fakerId,
                                             @Param("offset") int offset,
                                             @Param("pageSize") int pageSize);

}
