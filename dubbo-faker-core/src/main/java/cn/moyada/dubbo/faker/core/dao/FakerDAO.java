package cn.moyada.dubbo.faker.core.dao;

import cn.moyada.dubbo.faker.core.model.domain.AppInfoDO;
import cn.moyada.dubbo.faker.core.model.domain.LogDO;
import cn.moyada.dubbo.faker.core.model.domain.MethodInvokeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FakerDAO {

    void saveInvokeInfo(MethodInvokeDO methodInvokeDO);

    List<MethodInvokeDO> findAll();

    List<AppInfoDO> findAllApp();

    void updateUrl(@Param("groupId") String groupId, @Param("artifactId") String artifactId, @Param("url") String url);

    AppInfoDO findAppById(@Param("appId") int id);

    List<AppInfoDO> findDependencyById(@Param("ids") int[]ids);

    MethodInvokeDO findInvokeInfoById(@Param("id") int id);

    List<String> findParamByType(@Param("appId") int appId, @Param("type") String type);

    void saveLog(LogDO logDO);

    void saveLogList(@Param("list") List<LogDO> logDOs);

    List<String> findClassByApp(@Param("appId") int appId);

    List<MethodInvokeDO> findMethodByClass(@Param("className") String className);

    int countMethodByFakerId(@Param("fakerId") String fakerId);

    List<LogDO> findMethodByFakerId(@Param("fakerId") String fakerId,
                                             @Param("offset") int offset,
                                             @Param("pageSize") int pageSize);
}
