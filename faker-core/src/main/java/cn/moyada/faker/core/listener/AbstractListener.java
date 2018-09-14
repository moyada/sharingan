package cn.moyada.faker.core.listener;


import cn.moyada.faker.core.common.QuestInfo;
import cn.moyada.faker.core.handler.InvokeRecordHandler;
import cn.moyada.faker.core.handler.RecordHandler;
import cn.moyada.faker.core.queue.AbstractQueue;
import cn.moyada.faker.core.task.TaskEnvironment;
import cn.moyada.faker.manager.FakerManager;
import cn.moyada.faker.manager.domain.LogDO;
import cn.moyada.faker.rpc.api.invoke.InvokeCallback;
import cn.moyada.faker.rpc.api.invoke.Result;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 监听器
 * @author xueyikang
 * @create 2018-03-18 17:12
 */
public abstract class AbstractListener implements ListenerAction, InvokeCallback {

    protected FakerManager fakerManager;

    protected final RecordHandler<LogDO> recordHandler;

    protected final AbstractQueue<LogDO> queue;

    private volatile boolean isFinish = false;

    private final int total;
    private final AtomicInteger countDown;

    private Thread sleepThread;

    @Override
    public void setFakerManager(FakerManager fakerManager) {
        this.fakerManager = fakerManager;
    }

    protected AbstractListener(TaskEnvironment env, AbstractQueue<LogDO> queue) {
        QuestInfo invokerInfo = env.getQuestInfo();
        this.recordHandler = new InvokeRecordHandler(env.getFakerId(), invokerInfo.getInvokeId(),
                invokerInfo.isSaveResult(), invokerInfo.getResultParam());
        this.queue = queue;
        this.total = invokerInfo.getQuestNum();
        this.countDown = new AtomicInteger(0);
    }

    @Override
    public void callback(Result result) {
        LogDO receive = recordHandler.receive(result);
        queue.offer(receive);

        int current = countDown.incrementAndGet();
        if (current == total) {
            isFinish = true;
        }

        if (sleepThread != null) {
            LockSupport.unpark(this.sleepThread);
        }
    }

    @Override
    public void waitFinish() {
        while (!isFinish) {
            this.sleepThread = Thread.currentThread();
            LockSupport.park();
        }
    }
}
