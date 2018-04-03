package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.listener.AbstractListener;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.thread.PriorityThread;

/**
 * @author xueyikang
 * @create 2018-03-27 12:19
 */
public class DefaultInvoker extends AbstractInvoker {

    public DefaultInvoker(MethodProxy proxy, AbstractListener abstractListener, int poolSize) {
        super(proxy, abstractListener, poolSize);
    }

    @Override
    public void invoke(Object[] argsValue) {
//        super.count.increment();
        this.excutor.execute(new PriorityThread(() -> execute(argsValue)));
    }
}
