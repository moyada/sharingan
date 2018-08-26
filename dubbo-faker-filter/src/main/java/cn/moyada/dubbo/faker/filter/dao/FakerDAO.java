package cn.moyada.dubbo.faker.filter.dao;

import cn.moyada.dubbo.faker.filter.domain.AppInfoDO;
import cn.moyada.dubbo.faker.filter.domain.MethodInvokeDO;
import cn.moyada.dubbo.faker.filter.domain.RealParamDO;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author xueyikang
 * @create 2018-03-30 01:11
 */
public interface FakerDAO {

    /**
     * 查询方法调用信息是否存在
     * @param methodInvokeDO
     * @return
     */
    Integer existsInvokeInfo(MethodInvokeDO methodInvokeDO);

    /**
     * 创建方法调用信息
     * @param methodInvokeDO
     * @return
     */
    Integer saveInvokeInfo(MethodInvokeDO methodInvokeDO);

    /**
     * 查询项目编号
     * @param appName
     * @return
     */
    Integer findAppId(String appName);

    /**
     * 创建项目信息
     * @param appInfoDO
     */
    void saveApp(AppInfoDO appInfoDO);

    /**
     * 查询最大项目编号
     * @return
     */
    Integer findLastAppId();

    /**
     * 保存请求参数
     * @param paramDOs
     * @param timestamp
     */
    void saveParam(@Param("list") Set<RealParamDO> paramDOs, @Param("data") Timestamp timestamp);
}
