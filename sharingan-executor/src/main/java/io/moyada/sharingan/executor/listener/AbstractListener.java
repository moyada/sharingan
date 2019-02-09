package io.moyada.sharingan.executor.listener;


import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.task.ListenerAction;
import io.moyada.sharingan.domain.task.RecordHandler;
import io.moyada.sharingan.domain.task.ReportData;
import io.moyada.sharingan.infrastructure.invoke.InvokeReceiver;
import io.moyada.sharingan.infrastructure.invoke.data.Result;

import java.util.Queue;
import java.util.concurrent.locks.LockSupport;

/**
 * 监听器
 * @author xueyikang
 * @create 2018-03-18 17:12
 */
public abstract class AbstractListener<R extends InvokeResult> implements ListenerAction, InvokeReceiver {

    /**
     * 调用记录
     */
    private final ReportData listenerReportData;

    /**
     * 回调处理器
     */
    private final RecordHandler<R> recordHandler;

    /**
     * 请求队列
     */
    private final Queue<Result> queue;

    /**
     * 未处理请求
     */
    private volatile int countDown;

    /**
     * 等待主线程
     */
    private volatile Thread currentThread = null;

    AbstractListener(RecordHandler<R> recordHandler, ReportData listenerReportData,
                     Queue<Result> queue, int totalCount) {
        this.recordHandler = recordHandler;
        this.listenerReportData = listenerReportData;
        this.queue = queue;
        this.countDown = totalCount;
    }

    @Override
    public void callback(Result result) {
        queue.offer(result);
    }

    /**
     * 获取处理数据
     * @return
     */
    R pickUp() {
        Result result = queue.poll();
        if (null == result) {
            return null;
        }
        R receive = recordHandler.receive(result);
        listenerReportData.record(null == receive.getErrorMsg(), receive.getResponseTime());
        return receive;
    }

    /**
     * 单线程处理计时器
     * @param num
     * @return
     */
    boolean release(int num) {
        countDown = countDown - num;
        return countDown <= 0;
    }

    @Override
    public void process() {
        Thread pthread = new Thread(this::execution);
        pthread.setName("listener-thread");
        pthread.start();
    }

    /**
     * 处理数据
     */
    protected abstract void execution();

    @Override
    public ReportData waitFinish() {
        do {
            if (countDown == 0) {
                return listenerReportData;
            }
            currentThread = Thread.currentThread();
            LockSupport.park();
        } while (true);
    }

    /**
     * 唤醒等待线程
     */
    void notifyWait() {
        if (null == currentThread) {
            return;
        }
        LockSupport.unpark(currentThread);
    }
}
