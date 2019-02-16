package io.moyada.sharingan.monitor.mysql.handler;

import io.moyada.sharingan.monitor.api.exception.MonitorException;
import io.moyada.sharingan.monitor.mysql.config.DataMapperConfig;
import io.moyada.sharingan.monitor.mysql.data.Record;
import io.moyada.sharingan.monitor.mysql.support.SqlBuilder;
import io.moyada.sharingan.monitor.mysql.support.SqlExecutor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DataRepositoryImpl implements DataRepository {

    private final Map<String, Integer> cacheApp = new HashMap<>();

    private SqlBuilder sqlBuilder;
    private SqlExecutor sqlExecutor;

    private DataMapperConfig dataMapperConfig;

    public DataRepositoryImpl(SqlExecutor sqlExecutor, SqlBuilder sqlBuilder, DataMapperConfig dataMapperConfig) {
        this.sqlExecutor = sqlExecutor;
        this.sqlBuilder = sqlBuilder;
        this.dataMapperConfig = dataMapperConfig;
    }

    @Override
    public Integer getAppId(String appName) {
        Integer result = cacheApp.get(appName);
        if (null != result) {
            return result;
        }

        String findSql = sqlBuilder.buildFindAppId(appName);
        try {
            result = sqlExecutor.queryInt(findSql);
        } catch (SQLException e) {
            throw new MonitorException(findSql, e);
        }
        if (null != result) {
            cacheApp.put(appName, result);
        }

        return result;
    }

    @Override
    public void save(Collection<Record> records) {
        String insertSql = dataMapperConfig.getInsertSql(records);
        try {
            sqlExecutor.execute(insertSql);
        } catch (SQLException e) {
            throw new MonitorException(insertSql, e);
        }
    }
}
