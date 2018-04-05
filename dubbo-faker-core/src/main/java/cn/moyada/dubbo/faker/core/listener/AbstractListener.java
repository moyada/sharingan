package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.convert.LoggingConvert;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.InvokerInfo;
import cn.moyada.dubbo.faker.core.model.queue.UnlockQueue;

/**
 * 监听器
 * @author xueyikang
 * @create 2018-03-18 17:12
 */
public abstract class AbstractListener implements ListenerAction {

    protected final LoggingConvert convert;

    protected final FakerManager fakerManager;

    protected final UnlockQueue<InvokeFuture> futureQueue;

    protected AbstractListener(String fakerId, InvokerInfo invokerInfo,
                               UnlockQueue<InvokeFuture> queue, FakerManager fakerManager) {
        this.futureQueue = queue;
        this.fakerManager = fakerManager;
        this.convert = new LoggingConvert(fakerId, invokerInfo.getInvokeId(),
                invokerInfo.isSaveResult(), invokerInfo.getResultParam());
    }
}
