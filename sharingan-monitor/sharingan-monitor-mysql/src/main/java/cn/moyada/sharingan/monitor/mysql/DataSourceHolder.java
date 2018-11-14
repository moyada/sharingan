package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.ThreadUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.DataSourceException;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.Closeable;

/**
 * 数据库连接
 * @author xueyikang
 * @since 1.0
 **/
public class DataSourceHolder implements Closeable {

    private HikariDataSource dataSource;

    private SqlSessionFactory factory;

    public DataSourceHolder(MysqlConfig mysqlConfig) {
        if (null == mysqlConfig || mysqlConfig.isInvalid()) {
            throw new DataSourceException("connect parameter invalid.");
        }


        this.dataSource = getDataSource(mysqlConfig);
        ThreadUtil.addShutdownHook(new Runnable() {
            @Override
            public void run() {
                dataSource.close();
            }
        });

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("sharinganMonitor", transactionFactory, dataSource);

        Configuration configuration = new Configuration(environment);
        configuration.setLazyLoadingEnabled(true);

        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        this.factory = builder.build(configuration);
    }

    private HikariDataSource getDataSource(MysqlConfig mysqlConfig) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(mysqlConfig.getDriverClassName());
        config.setJdbcUrl(mysqlConfig.getUrl());
        config.setUsername(mysqlConfig.getUsername());
        config.setPassword(mysqlConfig.getPassword());
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(3_000);
        config.setMaxLifetime(30_000);
        return new HikariDataSource(config);
    }

    /**
     * 建立新会话
     * @return
     */
    public SqlSession openSession() {
        return factory.openSession();
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
