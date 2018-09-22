package cn.moyada.sharingan.core.listener;


import cn.moyada.sharingan.core.handler.InvokeRecordHandler;
import cn.moyada.sharingan.core.handler.RecordHandler;
import cn.moyada.sharingan.rpc.api.invoke.InvokeReceiver;
import cn.moyada.sharingan.rpc.api.invoke.Result;
import cn.moyada.sharingan.storage.api.InvocationRepository;
import cn.moyada.sharingan.storage.api.domain.InvocationResultDO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.locks.LockSupport;

/**
 * 监听器
 * @author xueyikang
 * @create 2018-03-18 17:12
 */
public abstract class AbstractListener extends ListenerReport implements ListenerAction, InvokeReceiver {

    private static final Logger log = LogManager.getLogger(AbstractListener.class);

    /**
     * 数据持久化
     */
    protected InvocationRepository invocationRepository;

    /**
     * 回调处理器
     */
    private final RecordHandler<InvocationResultDO> recordHandler;

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

    protected AbstractListener(InvokeRecordHandler recordHandler, Queue<Result> queue, int totalCount) {
        super(totalCount);
        this.recordHandler = recordHandler;
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
    protected InvocationResultDO pickUp() {
        Result result = queue.poll();
        if (null == result) {
            return null;
        }
        InvocationResultDO receive = recordHandler.receive(result);
        record(receive);
        return receive;
    }

    /**
     * 单线程处理计时器
     * @param num
     * @return
     */
    protected boolean release(int num) {
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
    public void waitFinish() {
        do {
            if (countDown == 0) {
                return;
            }
            currentThread = Thread.currentThread();
            LockSupport.park();
        } while (true);
    }

    /**
     * 唤醒等待线程
     */
    protected void notifyWait() {
        if (null == currentThread) {
            return;
        }
        LockSupport.unpark(currentThread);
    }

    public void setInvocationRepository(InvocationRepository invocationRepository) {
        this.invocationRepository = invocationRepository;
    }
}
