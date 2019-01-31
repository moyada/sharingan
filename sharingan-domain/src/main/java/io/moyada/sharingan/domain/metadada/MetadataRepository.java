package io.moyada.sharingan.domain.metadada;


import java.util.List;

/**
 * 元信息
 */
public interface MetadataRepository {

    /**
     * 获取所有项目信息
     * @return
     */
    List<AppData> findAllApp();

    /**
     * 通过编号获取项目信息
     * @param appId 项目id
     * @return
     */
    AppData findAppById(int appId);

    /**
     * 通过名称获取项目id
     * @param appName 项目名称
     * @return
     */
    AppData findAppByName(String appName);

    /**
     * 通过依赖获取项目信息
     * @param appIds 依赖项目id组合，i.e: 1,2,3
     * @return
     */
    List<AppData> findAppByIds(int[] appIds);

    /**
     * 通过项目编号获取服务信息
     * @param appId 项目id
     * @return
     */
    List<ServiceData> findServiceByApp(int appId);

    /**
     * 获取服务信息
     * @param serviceId 服务id
     * @return
     */
    ServiceData findServiceById(int serviceId);

    /**
     * 通过服务编号获取方法信息
     * @param serviceId 服务id
     * @return
     */
    List<MethodData> findMethodByService(int serviceId);

    /**
     * 获取方法信息
     * @param funId 方法id
     * @return
     */
    MethodData findMethodById(int funId);

    /**
     * 通过服务编号获取http请求信息
     * @param serviceId 服务id
     * @return
     */
    List<HttpData> findHttpByService(int serviceId);

    /**
     * 获取http请求信息
     * @param methodId 方法id
     * @return
     */
    HttpData findHttpById(int methodId);
}
