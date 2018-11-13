package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.entity.Record;
import cn.moyada.sharingan.monitor.api.handler.InvocationHandler;
import org.apache.ibatis.session.SqlSession;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MysqlHandler<T> implements InvocationHandler<Collection<Record<T>>> {

    private final SqlBuilder sqlBuilder;

    private final DataSourceHolder dataSourceHolder;

    public MysqlHandler(DataSourceHolder dataSourceHolder, SqlBuilder sqlBuilder) {
        this.dataSourceHolder = dataSourceHolder;
        this.sqlBuilder = sqlBuilder;
    }

    @Override
    public void handle(Collection<Record<T>> records) {
        String sql = sqlBuilder.insert(records);

        SqlSession sqlSession = dataSourceHolder.openSession();
        try {
            sqlSession.insert(sql);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
}
