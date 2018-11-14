package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.entity.Record;
import cn.moyada.sharingan.monitor.api.handler.InvocationHandler;
import cn.moyada.sharingan.monitor.api.processor.BatchInvocationWorker;
import cn.moyada.sharingan.monitor.api.receiver.InvocationReceiver;

import java.util.Collection;

/**
 * mysql 异步处理器
 * @author xueyikang
 * @since 1.0
 **/
public class MysqlWorker<T> extends BatchInvocationWorker<T> {

    public MysqlWorker(MysqlConfig mysqlConfig,
                       InvocationHandler<Collection<Record<T>>> handler,
                       InvocationReceiver<Record<T>> receiver)
    {
        super(handler, receiver, mysqlConfig.getIntervalTime(), mysqlConfig.getThresholdSize());
    }
}
