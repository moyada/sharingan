package cn.moyada.sharingan.monitor.api.receiver;

import cn.moyada.sharingan.monitor.api.entity.DefaultRecord;
import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.monitor.api.entity.Record;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DefaultInvocationReceiver implements InvocationReceiver<Record<String>> {

    @Override
    public Record<String> receive(Invocation invocation) {
        DefaultRecord record = new DefaultRecord();
        record.setApplication(invocation.getApplication());
        record.setDomain(invocation.getDomain());
        record.setProtocol(invocation.getProtocol());
        record.setArgs(invocation.getArgs().toString());
        return record;
    }
}
