package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.InvokerInfo;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.model.queue.UnlockQueue;

/**
 * 默认线程调用器
 * @author xueyikang
 * @create 2018-03-27 12:19
 */
public class DefaultInvoker extends AbstractInvoker {

    public DefaultInvoker(MethodProxy proxy, UnlockQueue<InvokeFuture> queue, InvokerInfo invokerInfo) {
        super(proxy, queue, invokerInfo);
    }

    @Override
    public void invoke(Object[] argsValue) {
//        super.count.increment();
        super.excutor.execute(() -> execute(argsValue));
    }
}
