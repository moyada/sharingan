package cn.moyada.sharingan.monitor.mysql;

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
 * @author xueyikang
 * @since 1.0
 **/
public class DataSourceHolder implements Closeable {

    private HikariDataSource dataSource;

    private SqlSessionFactory factory;

    public DataSourceHolder(MysqlConfig mysqlConfig) {
        if (null == mysqlConfig || mysqlConfig.isInvalid()) {
            throw new DataSourceException();
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(mysqlConfig.getUrl());
        config.setUsername(mysqlConfig.getUsername());
        config.setPassword(mysqlConfig.getPassword());

        this.dataSource = new HikariDataSource(config);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            dataSource.close();
        }));

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("sharinganMonitor", transactionFactory, dataSource);

        Configuration configuration = new Configuration(environment);
        configuration.setLazyLoadingEnabled(true);

        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        this.factory = builder.build(configuration);
    }

    public SqlSession openSession() {
        return factory.openSession();
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
