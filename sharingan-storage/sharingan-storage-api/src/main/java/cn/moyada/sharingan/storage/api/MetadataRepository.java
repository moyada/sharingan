package cn.moyada.sharingan.storage.api;

import cn.moyada.sharingan.storage.api.domain.AppDO;
import cn.moyada.sharingan.storage.api.domain.FunctionDO;
import cn.moyada.sharingan.storage.api.domain.ServiceDO;

import java.util.List;

public interface MetadataRepository {

    /**
     * 获取所有项目信息
     * @return
     */
    List<AppDO> findAllApp();

    /**
     * 通过编号获取项目信息
     * @param appId 项目id
     * @return
     */
    AppDO findAppById(int appId);

    /**
     * 通过名称获取项目id
     * @param appName 项目名称
     * @return
     */
    AppDO findAppByName(String appName);

    /**
     * 通过依赖获取项目信息
     * @param dependencies 依赖项目id组合，i.e: 1,2,3
     * @return
     */
    List<AppDO> findApp(String dependencies);

    /**
     * 通过项目编号获取服务信息
     * @param appId 项目id
     * @return
     */
    List<ServiceDO> findServiceByApp(int appId);

    /**
     * 获取服务信息
     * @param serviceId 服务id
     * @return
     */
    ServiceDO findServiceById(int serviceId);

    /**
     * 通过服务编号获取方法信息
     * @param serviceId 服务id
     * @return
     */
    List<FunctionDO> findFunctionByService(int serviceId);

    /**
     * 获取方法信息
     * @param funId 方法id
     * @return
     */
    FunctionDO findFunctionById(int funId);
}
