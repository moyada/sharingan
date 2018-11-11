package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.processor.BatchInvocationWorker;
import cn.moyada.sharingan.monitor.api.receiver.DefaultInvocationReceiver;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MysqlWorker extends BatchInvocationWorker {

    public MysqlWorker(int intervalTime, int thresholdSize) {
        super(new MysqlHandler(), new DefaultInvocationReceiver(), intervalTime, thresholdSize);
    }
}
