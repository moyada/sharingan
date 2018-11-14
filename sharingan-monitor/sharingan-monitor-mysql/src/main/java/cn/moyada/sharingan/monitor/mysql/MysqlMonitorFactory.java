package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.AbstractMonitor;
import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.api.ThreadUtil;
import cn.moyada.sharingan.monitor.api.entity.Record;
import cn.moyada.sharingan.monitor.api.handler.InvocationHandler;
import cn.moyada.sharingan.monitor.api.processor.AbstractInvocationWorker;
import cn.moyada.sharingan.monitor.api.receiver.InvocationReceiver;

import java.util.Collection;

/**
 * mysql 监视器工厂
 * @author xueyikang
 * @since 1.0
 **/
public class MysqlMonitorFactory {

    public static Monitor build(MysqlConfig mysqlConfig) {
        DataSourceHolder dataSourceHolder = new DataSourceHolder(mysqlConfig);
        SqlBuilder sqlBuilder = new SqlBuilder(mysqlConfig.getTable(), mysqlConfig.getColumn());

        InvocationHandler<Collection<Record<String>>> handler = new MysqlHandler<>(dataSourceHolder, sqlBuilder);
        InvocationReceiver<Record<String>> receiver = new JsonInvocationReceiver();

        AbstractInvocationWorker<Record<String>> mysqlWorker = new MysqlWorker<>(mysqlConfig, handler, receiver);

        ThreadUtil.addShutdownHook(mysqlWorker);
        return new AbstractMonitor(mysqlWorker) {};
    }
}
