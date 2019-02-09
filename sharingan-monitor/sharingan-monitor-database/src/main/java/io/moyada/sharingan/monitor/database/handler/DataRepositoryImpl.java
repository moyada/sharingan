package io.moyada.sharingan.monitor.database.handler;

import io.moyada.sharingan.monitor.database.support.SqlBuilder;
import io.moyada.sharingan.monitor.database.support.DataSourceHolder;
import io.moyada.sharingan.monitor.database.data.Record;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DataRepositoryImpl implements DataRepository {

    private final Map<String, Integer> cacheApp = new HashMap<>();

    private DataSourceHolder dataSourceHolder;

    private SqlBuilder sqlBuilder;

    public DataRepositoryImpl(DataSourceHolder dataSourceHolder, SqlBuilder sqlBuilder) {
        this.dataSourceHolder = dataSourceHolder;
        this.sqlBuilder = sqlBuilder;
    }

    @Override
    public Integer getAppId(String appName) {
        Integer result = cacheApp.get(appName);
        if (null != result) {
            return result;
        }

        String findSql = sqlBuilder.findAppId(appName);
        Connection connect = dataSourceHolder.getConnect();

        try {
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(findSql);

            if (resultSet.next()) {
                result = resultSet.getInt(1);
                cacheApp.put(appName, result);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void save(Collection<Record> records) {
        String insertSql = sqlBuilder.insert(records);
        Connection connect = dataSourceHolder.getConnect();

        try {
            Statement statement = connect.createStatement();
            statement.execute(insertSql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
