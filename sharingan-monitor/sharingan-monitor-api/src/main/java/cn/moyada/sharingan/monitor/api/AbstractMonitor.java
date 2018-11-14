package cn.moyada.sharingan.monitor.api;

import cn.moyada.sharingan.monitor.api.entity.Invocation;
import cn.moyada.sharingan.monitor.api.processor.InvocationWorker;

/**
 * 异步处理监视器
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AbstractMonitor implements Monitor {

    protected InvocationWorker worker;

    public AbstractMonitor(InvocationWorker worker) {
        this.worker = worker;
        startWorker();
    }

    private void startWorker() {
        new Thread(worker).start();
    }

    @Override
    public void listener(Invocation invocation) {
        worker.submit(invocation);
    }
}
