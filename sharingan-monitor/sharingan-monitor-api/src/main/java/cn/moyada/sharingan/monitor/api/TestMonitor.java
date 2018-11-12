package cn.moyada.sharingan.monitor.api;

import cn.moyada.sharingan.monitor.api.entity.Record;
import cn.moyada.sharingan.monitor.api.handler.InvocationHandler;
import cn.moyada.sharingan.monitor.api.processor.BatchInvocationWorker;
import cn.moyada.sharingan.monitor.api.receiver.DefaultInvocationReceiver;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class TestMonitor extends AbstractMonitor {

    public TestMonitor() {
        super(new BatchInvocationWorker(new InvocationHandler<Collection<Record>>() {
            @Override
            public void handle(Collection<Record> records) {
                System.out.println("run task " + records);
            }
        }, new DefaultInvocationReceiver(), 2000, 10));
    }
}
