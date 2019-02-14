package io.moyada.sharingan.monitor.mysql.support;

import io.moyada.sharingan.monitor.mysql.config.DataSourceConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class SqlExecutor extends DataSourceHolder {

    public SqlExecutor(DataSourceConfig config) {
        super(config);
    }

    public Integer queryInt(String sql) throws SQLException {
        Integer result = null;

        Statement statement = null;
        ResultSet resultSet = null;

        Connection connect = getConnect();

        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (null != statement) {
                statement.close();
            }
            if (null != resultSet) {
                resultSet.close();
            }
        }

        return result;
    }

    public void execute(String sql) throws SQLException {
        Connection connect = getConnect();

        Statement statement = null;
        try {
            statement = connect.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (null != statement) {
                statement.close();
            }
        }
    }
}
