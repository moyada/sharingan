package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.exception.UnsupportedParamNumberException;
import cn.moyada.dubbo.faker.core.listener.CompletedListener;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;

import java.lang.invoke.MethodHandle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

public abstract class AbstractInvoker {

    // cpu核心数*2
    private final static int CPU_COUNT_MUL_2 = Runtime.getRuntime().availableProcessors() * 2;

    private final CompletedListener completedListener;

    protected final ExecutorService excutor;
    protected final LongAdder count;

    protected final MethodHandle handle;
    protected final Object service;

    public AbstractInvoker(MethodHandle handle, Object service,
                           CompletedListener completedListener, int poolSize) {
        this.excutor = Executors.newFixedThreadPool(CPU_COUNT_MUL_2 > poolSize ? poolSize : CPU_COUNT_MUL_2);
        this.completedListener = completedListener;
        this.count = new LongAdder();
        this.handle = handle;
        this.service = service;
    }

    /**
     * 调用请求
     * @param argsValue
     * @param realParam
     */
    public abstract void invoke(Object[] argsValue, String realParam);

    /**
     * 等待所有任务完成关闭线程池
     */
    public void destroy() {
        while (count.longValue() != 0) {
            LockSupport.parkNanos(10000);
        }
        excutor.shutdown();
    }

    /**
     * 调用接口请求
     * @param argsValue 参数
     * @return
     * @throws Throwable
     */
    protected Object execute(Object[] argsValue) throws Throwable {
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

    /**
     * 记录调用结果
     * @param result
     */
    protected void callback(InvokeFuture result) {
        completedListener.record(result);
    }
}
