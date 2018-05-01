package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.common.BeanHolder;
import cn.moyada.dubbo.faker.core.convert.LoggingConvert;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.InvokerInfo;
import cn.moyada.dubbo.faker.core.model.queue.AbstractQueue;

/**
 * 监听器
 * @author xueyikang
 * @create 2018-03-18 17:12
 */
public abstract class AbstractListener implements ListenerAction {

    protected FakerManager fakerManager;

    protected final LoggingConvert convert;

    protected final AbstractQueue<InvokeFuture> futureQueue;

    protected AbstractListener(String fakerId, InvokerInfo invokerInfo, AbstractQueue<InvokeFuture> queue) {
        this.futureQueue = queue;
        this.convert = new LoggingConvert(fakerId, invokerInfo.getInvokeId(),
                invokerInfo.isSaveResult(), invokerInfo.getResultParam());
        setFakerManager();
    }

    private void setFakerManager() {
        this.fakerManager = BeanHolder.getBean(FakerManager.class);
    }
}
