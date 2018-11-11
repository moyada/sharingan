package cn.moyada.sharingan.monitor.api;

import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.monitor.api.processor.InvocationWorker;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractMonitor implements Monitor {

    private InvocationWorker worker;

    public AbstractMonitor(InvocationWorker worker) {
        this.worker = worker;
    }

    @Override
    public void listener(Invocation invocation) {
        worker.work(invocation);
    }
}
