package cn.moyada.sharingan.monitor.api.receiver;

import cn.moyada.sharingan.monitor.api.entity.DefaultRecoed;
import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.monitor.api.entity.Record;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DefaultInvocationReceiver implements InvocationReceiver<Record> {

    @Override
    public Record receive(Invocation invocation) {
        DefaultRecoed record = new DefaultRecoed();
        record.setApplication(invocation.getApplication());
        record.setDomain(invocation.getDomain());
        record.setProtocol(invocation.getProtocol());
        record.setArgs(invocation.getArgs().toString());
        return record;
    }
}
