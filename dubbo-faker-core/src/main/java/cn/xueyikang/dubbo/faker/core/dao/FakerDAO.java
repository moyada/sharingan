package cn.xueyikang.dubbo.faker.core.dao;

import cn.xueyikang.dubbo.faker.core.model.LogDO;
import cn.xueyikang.dubbo.faker.core.model.MethodInvokeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FakerDAO {

    void saveInvokeInfo(MethodInvokeDO methodInvokeDO);

    List<MethodInvokeDO> findAll();

    MethodInvokeDO findInvokeInfoById(@Param("id") int id);

    List<String> findParamByType(@Param("appId") int appId, @Param("type") String type);

    void saveLog(LogDO logDO);

    List<MethodInvokeDO> findAllApp();

    List<String> findClassByApp(@Param("appId") int appId);

    List<MethodInvokeDO> findMethodByClass(@Param("className") String className);

    int countMethodByFakerId(@Param("fakerId") String fakerId);

    List<LogDO> findMethodByFakerId(@Param("fakerId") String fakerId,
                                             @Param("offset") int offset,
                                             @Param("pageSize") int pageSize);

}
