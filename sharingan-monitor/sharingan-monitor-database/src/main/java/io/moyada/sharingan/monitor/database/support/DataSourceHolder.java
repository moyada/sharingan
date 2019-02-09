package io.moyada.sharingan.monitor.database.support;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.moyada.sharingan.monitor.api.util.ThreadUtil;
import io.moyada.sharingan.monitor.database.config.DataSourceConfig;

import java.io.Closeable;
import java.sql.*;

/**
 * 数据库连接
 * @author xueyikang
 * @since 1.0
 **/
public class DataSourceHolder implements Closeable {

    private HikariDataSource dataSource;

    private Connection connection;

    public DataSourceHolder(DataSourceConfig config) {
        this.dataSource = getDataSource(config);

        ThreadUtil.addShutdownHook(new Runnable() {
            @Override
            public void run() {
                close();
            }
        });
    }

    private HikariDataSource getDataSource(DataSourceConfig mysqlConfig) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(mysqlConfig.getDriverClassName());
        config.setJdbcUrl(mysqlConfig.getUrl());
        config.setUsername(mysqlConfig.getUsername());
        config.setPassword(mysqlConfig.getPassword());
        config.setAutoCommit(true);
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(3_000);
        config.setMaxLifetime(30_000);
        return new HikariDataSource(config);
    }

    /**
     * 获取连接
     * @return
     */
    public Connection getConnect() {
        if (null == connection) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataSource.close();
    }
}
