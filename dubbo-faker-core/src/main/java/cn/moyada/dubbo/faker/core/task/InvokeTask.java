package cn.moyada.dubbo.faker.core.task;

import cn.moyada.dubbo.faker.core.invoke.AbstractInvoker;
import cn.moyada.dubbo.faker.core.invoke.DefaultInvoker;
import cn.moyada.dubbo.faker.core.listener.AbstractListener;
import cn.moyada.dubbo.faker.core.listener.BatchLoggingListener;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.InvokerInfo;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.model.queue.AbstractQueue;
import cn.moyada.dubbo.faker.core.model.queue.ArrayQueue;
import cn.moyada.dubbo.faker.core.model.queue.AtomicQueue;
import cn.moyada.dubbo.faker.core.model.queue.UnlockQueue;
import cn.moyada.dubbo.faker.core.provider.ParamProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

import static cn.moyada.dubbo.faker.core.common.Constant.NANO_PER_MILLIS;

/**
 * 调用任务
 * @author xueyikang
 * @create 2018-04-05 15:13
 */
public class InvokeTask {
    private static final Logger log = LoggerFactory.getLogger(InvokeTask.class);

    private final String fakerId;

    private final int questNum;

    // 参数提供器
    private final ParamProvider paramProvider;

    // 方法调用器
    private final AbstractInvoker invoker;

    // 结果监听器
    private final AbstractListener listener;

    public InvokeTask(MethodProxy proxy, InvokerInfo invokerInfo) {
        this.fakerId = proxy.getFakerId();
        this.questNum = invokerInfo.getQuestNum();

        final AbstractQueue<InvokeFuture> queue;
        switch (invokerInfo.getPoolSize()) {
            case 1:
                queue = new ArrayQueue<>(invokerInfo.getQuestNum());
                break;
            case 2:
                queue = new AtomicQueue<>(invokerInfo.getQuestNum());
                break;
            default:
                queue = UnlockQueue.build(invokerInfo.getPoolSize(), invokerInfo.getQuestNum());
        }

        listener = new BatchLoggingListener(proxy.getFakerId(), invokerInfo, queue);

        invoker = new DefaultInvoker(proxy, queue, invokerInfo);

        paramProvider = new ParamProvider(proxy.getValues(), proxy.getParamTypes(), invokerInfo.isRandom());
    }

    public void shutdown() {
        invoker.shutdownDelay();
        log.info("completed invoke: " + fakerId);
    }

    private void listener() {
        log.info("start listener.");
        listener.startListener();
    }

    public void start() {
        listener();
        log.info("start faker invoke: " + fakerId);
        for (int index = 0; index < questNum; index++) {
            invoker.invoke(paramProvider.fetchNextParam());
        }
    }

    public void start(int timeout) {
        listener();
        log.info("start timeout faker invoke: " + fakerId);
        for (int index = 0; index < questNum; index++) {
            invoker.invoke(paramProvider.fetchNextParam());
            LockSupport.parkNanos(timeout * NANO_PER_MILLIS);
        }
    }
}
