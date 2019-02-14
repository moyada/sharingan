package io.moyada.sharingan.monitor.mysql.handler;

import io.moyada.sharingan.monitor.api.entity.AppInfo;
import io.moyada.sharingan.monitor.api.entity.FunctionInfo;
import io.moyada.sharingan.monitor.api.entity.HttpInfo;
import io.moyada.sharingan.monitor.api.entity.ServiceInfo;
import io.moyada.sharingan.monitor.api.exception.RegisterException;
import io.moyada.sharingan.monitor.mysql.support.SqlBuilder;
import io.moyada.sharingan.monitor.mysql.support.SqlExecutor;

import java.sql.SQLException;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MetadataRepositoryImpl implements MetadataRepository {

    private SqlExecutor sqlExecutor;
    private SqlBuilder sqlBuilder;

    public MetadataRepositoryImpl(SqlExecutor sqlExecutor, SqlBuilder sqlBuilder) {
        this.sqlExecutor = sqlExecutor;
        this.sqlBuilder = sqlBuilder;
    }

    @Override
    public Integer findAppId(String name) throws RegisterException {
        String sql = sqlBuilder.buildFindAppId(name);
        try {
            return sqlExecutor.queryInt(sql);
        } catch (SQLException e) {
            throw new RegisterException(sql, e);
        }
    }

    @Override
    public Integer findServiceId(int appId, String name) {
        String sql = sqlBuilder.buildFindServiceId(appId, name);
        try {
            return sqlExecutor.queryInt(sql);
        } catch (SQLException e) {
            throw new RegisterException(sql, e);
        }
    }

    @Override
    public Integer findFunctionId(int appId, int serviceId, String name) {
        String sql = sqlBuilder.buildFindFunctionId(appId, serviceId, name);
        try {
            return sqlExecutor.queryInt(sql);
        } catch (SQLException e) {
            throw new RegisterException(sql, e);
        }
    }

    @Override
    public Integer findHttpId(int appId, int serviceId, String name) {
        String sql = sqlBuilder.buildFindHttpId(appId, serviceId, name);
        try {
            return sqlExecutor.queryInt(sql);
        } catch (SQLException e) {
            throw new RegisterException(sql, e);
        }
    }

    @Override
    public void saveApp(AppInfo appInfo) {
        String sql = sqlBuilder.buildInsertAppSql(appInfo);

        try {
            sqlExecutor.execute(sql);
        } catch (SQLException e) {
            throw new RegisterException(sql, e);
        }
    }

    @Override
    public void saveService(ServiceInfo serviceInfo) {
        String sql = sqlBuilder.buildInsertServiceSql(serviceInfo);

        try {
            sqlExecutor.execute(sql);
        } catch (SQLException e) {
            throw new RegisterException(sql, e);
        }
    }

    @Override
    public void saveFunction(FunctionInfo functionInfo) {
        String sql = sqlBuilder.buildInsertFunctionSql(functionInfo);

        try {
            sqlExecutor.execute(sql);
        } catch (SQLException e) {
            throw new RegisterException(sql, e);
        }
    }

    @Override
    public void saveHttp(HttpInfo httpInfo) {
        String sql = sqlBuilder.buildInsertHttpSql(httpInfo);

        try {
            sqlExecutor.execute(sql);
        } catch (SQLException e) {
            throw new RegisterException(sql, e);
        }
    }
}
