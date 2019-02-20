package io.moyada.sharingan.monitor.mysql.support;


import io.moyada.sharingan.monitor.api.entity.*;
import io.moyada.sharingan.monitor.api.util.StringUtil;
import io.moyada.sharingan.monitor.mysql.config.FindAction;
import io.moyada.sharingan.monitor.mysql.config.MetadataConfig;

/**
 * SQL 构造器
 * @author xueyikang
 * @since 1.0
 **/
public class SqlBuilder {

    private final MetadataConfig metadataConfig;

    public SqlBuilder(MetadataConfig metadataConfig) {
        this.metadataConfig = metadataConfig;
    }

    private String buildFindSql(FindAction findAction, Integer appId, Integer serviceId, String value) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `").append(findAction.getKey()).append("` FROM ").append(findAction.getTable())
        .append(" WHERE ");
        if (null != appId) {
            sql.append("`app_id` = ").append(appId).append(" AND ");
        }
        if (null != serviceId) {
            sql.append("`service_id` = ").append(serviceId).append(" AND ");
        }

        return sql.append("`").append(findAction.getCondition()).append("` = '").append(value).append("';").toString();
    }

    public String buildFindAppId(String application) {
        return buildFindSql(metadataConfig.getAppConfig(), null, null, application);
    }

    public String buildFindServiceId(int appId, String service) {
        return buildFindSql(metadataConfig.getServiceConfig(), appId, null, service);
    }

    public String buildFindFunctionId(int appId, int serviceId, String function) {
        return buildFindSql(metadataConfig.getFunctionConfig(), appId, serviceId, function);
    }

    public String buildFindHttpId(int appId, int serviceId, String http) {
        return buildFindSql(metadataConfig.getHttpConfig(), appId, serviceId, http);
    }

    private String buildInsertSql(FindAction findAction, String value) {
        return "INSERT INTO `" + findAction.getTable() + "` (" + findAction.getColumn() + ") VALUE (" + value + ");";
    }

    public String buildInsertAppSql(AppInfo appInfo) {
        String value = StringUtil.concat(',',
                NameUtil.getValue(appInfo.getName()),
                NameUtil.getValue(appInfo.getGroupId()),
                NameUtil.getValue(appInfo.getArtifactId()));
        return buildInsertSql(metadataConfig.getAppConfig(), value);
    }

    public String buildInsertServiceSql(ServiceInfo serviceInfo) {
        Protocol protocol = serviceInfo.getProtocol();

        String value =  StringUtil.concat(',',
                serviceInfo.getAppId(),
                NameUtil.getValue(serviceInfo.getName()),
                NameUtil.getValue(protocol.name()),
                NameUtil.getValue(serviceInfo.getClassType()));
        return buildInsertSql(metadataConfig.getServiceConfig(), value);
    }

    public String buildInsertFunctionSql(FunctionInfo functionInfo) {
        String value = StringUtil.concat(',',
                functionInfo.getAppId(),
                functionInfo.getServiceId(),
                NameUtil.getValue(functionInfo.getClassType()),
                NameUtil.getValue(functionInfo.getName()),
                NameUtil.getValue(NameUtil.getName(functionInfo.getParamTypes())),
                NameUtil.getValue(functionInfo.getReturnType()));
        return buildInsertSql(metadataConfig.getFunctionConfig(), value);
    }

    public String buildInsertHttpSql(HttpInfo httpInfo) {
        String value = StringUtil.concat(',',
                httpInfo.getAppId(),
                httpInfo.getServiceId(),
                NameUtil.getValue(httpInfo.getName()),
                NameUtil.getValue(httpInfo.getType().name()),
                NameUtil.getValue(NameUtil.getName(httpInfo.getParam())),
                NameUtil.getValue(NameUtil.getName(httpInfo.getHeader())));
        return buildInsertSql(metadataConfig.getHttpConfig(), value);
    }

}
