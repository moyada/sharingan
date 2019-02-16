package io.moyada.sharingan.monitor.mysql.handler;

import io.moyada.sharingan.monitor.api.entity.AppInfo;
import io.moyada.sharingan.monitor.api.entity.FunctionInfo;
import io.moyada.sharingan.monitor.api.entity.HttpInfo;
import io.moyada.sharingan.monitor.api.entity.ServiceInfo;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface MetadataRepository {

    Integer findAppId(String name);

    Integer findServiceId(int appId, String name);

    Integer findFunctionId(int appId, int serviceId, String name);

    Integer findHttpId(int appId, int serviceId, String name);

    void saveApp(AppInfo appInfo);

    void saveService(ServiceInfo serviceInfo);

    void saveFunction(FunctionInfo functionInfo);

    void saveHttp(HttpInfo httpInfo);
}
