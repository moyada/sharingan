package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.exception.UnsupportedParamNumberException;
import cn.moyada.dubbo.faker.core.listener.CompletedListener;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;

import java.lang.invoke.MethodHandle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.LongAdder;

public abstract class AbstractInvoke {

    private final CompletedListener completedListener;
    protected final ExecutorService excutor;
    protected final LongAdder count;

    public AbstractInvoke(ExecutorService excutor, CompletedListener completedListener) {
        this.excutor = excutor;
        this.completedListener = completedListener;
        this.count = new LongAdder();
    }

    public abstract void invoke(MethodHandle handle, Object service, Object[] argsValue, String realParam);

    public void destroy() {
        while (count.longValue() != 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        excutor.shutdown();
    }

    protected Object execute(MethodHandle handle, Object service, Object[] argsValue) throws Throwable {
        if(null == argsValue) {
            return handle.invoke(service);
        }
        switch (argsValue.length) {
            case 0:
                return handle.invoke(service);
            case 1:
                return handle.invoke(service, argsValue[0]);
            case 2:
                return handle.invoke(service, argsValue[0], argsValue[1]);
            case 3:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2]);
            case 4:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3]);
            case 5:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4]);
            case 6:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5]);
            case 7:
                return handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5], argsValue[6]);
            default:
                return new UnsupportedParamNumberException("[Faker Invoker Error] Param number not yet support.");
        }
    }

    protected void callback(InvokeFuture result) {
        completedListener.record(result);
    }
}
