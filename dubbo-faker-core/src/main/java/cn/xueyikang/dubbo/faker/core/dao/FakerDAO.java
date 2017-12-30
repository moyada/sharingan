package cn.xueyikang.dubbo.faker.core.dao;

import cn.xueyikang.dubbo.faker.core.model.LogDO;
import cn.xueyikang.dubbo.faker.core.model.MethodInvokeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FakerDAO {

    void saveInvokeInfo(MethodInvokeDO methodInvokeDO);

    MethodInvokeDO findInvokeInfoById(@Param("id") int id);

    List<String> findParamByType(@Param("appId") int appId, @Param("type") String type);

    void saveLog(LogDO logDO);
}
