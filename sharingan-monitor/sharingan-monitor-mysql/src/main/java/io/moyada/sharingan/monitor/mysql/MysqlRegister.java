package io.moyada.sharingan.monitor.mysql;

import io.moyada.sharingan.monitor.api.Register;
import io.moyada.sharingan.monitor.api.entity.AppInfo;
import io.moyada.sharingan.monitor.api.entity.FunctionInfo;
import io.moyada.sharingan.monitor.api.entity.HttpInfo;
import io.moyada.sharingan.monitor.api.entity.ServiceInfo;
import io.moyada.sharingan.monitor.api.exception.RegisterException;
import io.moyada.sharingan.monitor.mysql.config.DataSourceConfig;
import io.moyada.sharingan.monitor.mysql.config.MetadataConfig;
import io.moyada.sharingan.monitor.mysql.handler.MetadataRepository;
import io.moyada.sharingan.monitor.mysql.handler.MetadataRepositoryImpl;
import io.moyada.sharingan.monitor.mysql.support.SqlBuilder;
import io.moyada.sharingan.monitor.mysql.support.SqlExecutor;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MysqlRegister implements Register {

    public static final String TYPE = "mysql";

    private final MetadataRepository metadataRepository;

    public MysqlRegister(DataSourceConfig dataSourceConfig, MetadataConfig metadataConfig) {
        if (null == dataSourceConfig) {
            throw new NullPointerException("DataSourceConfig is null");
        }
        if (null == metadataConfig) {
            throw new NullPointerException("MetadataConfig is null");
        }
        dataSourceConfig.checkParam();
        metadataConfig.checkParam();

        SqlExecutor sqlExecutor = new SqlExecutor(dataSourceConfig);
        SqlBuilder sqlBuilder = new SqlBuilder(metadataConfig);

        this.metadataRepository = new MetadataRepositoryImpl(sqlExecutor, sqlBuilder);
    }

    @Override
    public int addAppReturnId(AppInfo appInfo) throws RegisterException {
        String name = appInfo.getName();
        Integer appId = metadataRepository.findAppId(name);
        if (null != appId) {
            return appId;
        }

        metadataRepository.saveApp(appInfo);
        appId = metadataRepository.findAppId(name);
        if (null == appId){
            throw new RegisterException("can not find AppInfo id");
        }

        return appId;
    }

    @Override
    public int addServiceReturnId(ServiceInfo serviceInfo) throws RegisterException {
        int appId = serviceInfo.getAppId();
        String name = serviceInfo.getName();
        Integer serviceId = metadataRepository.findServiceId(appId, name);
        if (null != serviceId) {
            return serviceId;
        }

        metadataRepository.saveService(serviceInfo);
        serviceId = metadataRepository.findServiceId(appId, name);
        if (null == serviceId){
            throw new RegisterException("can not find ServiceInfo id");
        }

        return serviceId;
    }

    @Override
    public int addFunctionReturnId(FunctionInfo functionInfo) throws RegisterException {
        int appId = functionInfo.getAppId();
        int serviceId = functionInfo.getServiceId();
        String name = functionInfo.getName();
        Integer funcId = metadataRepository.findFunctionId(appId, serviceId, name);
        if (null != funcId) {
            return funcId;
        }

        metadataRepository.saveFunction(functionInfo);
        funcId = metadataRepository.findFunctionId(appId, serviceId, name);
        if (null == funcId){
            throw new RegisterException("can not find FunctionInfo id");
        }

        return funcId;
    }

    @Override
    public int addHttpReturnId(HttpInfo httpInfo) throws RegisterException {
        int appId = httpInfo.getAppId();
        int serviceId = httpInfo.getServiceId();
        String name = httpInfo.getName();
        Integer funcId = metadataRepository.findHttpId(appId, serviceId, name);
        if (null != funcId) {
            return funcId;
        }

        metadataRepository.saveHttp(httpInfo);
        funcId = metadataRepository.findHttpId(appId, serviceId, name);
        if (null == funcId){
            throw new RegisterException("can not find HttpInfo id");
        }

        return funcId;
    }
}
