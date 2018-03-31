package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.exception.UnsupportedParamNumberException;
import cn.moyada.dubbo.faker.core.listener.AbstractListener;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.utils.ParamUtil;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

public abstract class AbstractInvoker {

    private final static int MAX_POOL_SIZE;
    static {
        int cpuCore = Runtime.getRuntime().availableProcessors() * 2;
        MAX_POOL_SIZE = cpuCore % 8 == 0 ? cpuCore : cpuCore + (8 - cpuCore % 8);
    }

    private final AbstractListener abstractListener;

    protected final ExecutorService excutor;
    protected final LongAdder count;

    protected final MethodHandle handle;
    protected final Object service;
    protected final int poolSize;
    // protected final AtomicInteger curIndex;

    public AbstractInvoker(MethodHandle handle, Object service,
                           AbstractListener abstractListener, int poolSize) {
        poolSize = poolSize > MAX_POOL_SIZE ? MAX_POOL_SIZE : poolSize;
        this.excutor = new ThreadPoolExecutor(poolSize, MAX_POOL_SIZE, 30L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000), new ShutdownRejected());
        this.poolSize = poolSize;
        // this.curIndex = new AtomicInteger(0);
        this.abstractListener = abstractListener;
        this.count = new LongAdder();
        this.handle = handle;
        this.service = service;
    }

    /**
     * 调用请求
     * @param argsValue
     */
    public abstract void invoke(Object[] argsValue);

    /**
     * 等待所有任务完成关闭线程池
     */
    public void shutdownDelay() {
        while (count.longValue() != 0) {
            LockSupport.parkNanos(1_000_000L);
            if(Thread.currentThread().isInterrupted()) {
                break;
            }
        }
        excutor.shutdown();
    }

    /**
     * 调用接口请求
     * @param argsValue 参数
     */
    protected void execute(Object[] argsValue) {
        count.increment();

        // 开始时间
        long start = System.nanoTime();
        Timestamp invokeTime = Timestamp.from(Instant.now());

        FutureResult result;
        try {
            if(null != argsValue) {
                switch (argsValue.length) {
                    case 0:
                        result = FutureResult.success(handle.invoke(service));
                        break;
                    case 1:
                        result = FutureResult.success(handle.invoke(service, argsValue[0]));
                        break;
                    case 2:
                        result = FutureResult.success(handle.invoke(service, argsValue[0], argsValue[1]));
                        break;
                    case 3:
                        result = FutureResult.success(handle.invoke(service, argsValue[0], argsValue[1], argsValue[2]));
                        break;
                    case 4:
                        result = FutureResult.success(handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3]));
                        break;
                    case 5:
                        result = FutureResult.success(handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4]));
                        break;
                    case 6:
                        result = FutureResult.success(handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5]));
                        break;
                    case 7:
                        result = FutureResult.success(handle.invoke(service, argsValue[0], argsValue[1], argsValue[2], argsValue[3], argsValue[4], argsValue[5], argsValue[6]));
                        break;
                    default:
                        throw new UnsupportedParamNumberException("[Faker Invoker Error] Param number not yet support.");
                }
            }
            else {
                result = FutureResult.success(handle.invoke(service));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            result = FutureResult.failed(throwable.getMessage());
        }
        // 完成计算耗时
        result.setSpend((System.nanoTime() - start) / 1_000_000);
        callback(new InvokeFuture(result, invokeTime, ParamUtil.toString(argsValue)));
        count.decrement();
    }

    /**
     * 记录调用结果
     * @param result
     */
    protected void callback(InvokeFuture result) {
        abstractListener.record(result);
    }

    public class ShutdownRejected implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            executor.shutdownNow();
            abstractListener.shutdown();
            throw new RuntimeException("服务响应过慢，已强制关闭.");
        }
    }
}
