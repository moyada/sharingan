package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.exception.UnsupportedParamNumberException;
import cn.moyada.dubbo.faker.core.listener.AbstractListener;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.utils.DateUtil;
import cn.moyada.dubbo.faker.core.utils.ParamUtil;
import cn.moyada.dubbo.faker.core.utils.RuntimeUtil;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

import static cn.moyada.dubbo.faker.core.common.Constant.NANO_PER_MILLIS;

public abstract class AbstractInvoker {

    private final AbstractListener abstractListener;

    protected final ExecutorService excutor;
    protected final LongAdder count;

//    protected final MethodHandle handle;
//    protected final Object service;

    protected final MethodHandle[] methodHandles;
    protected final Object[] serviceAssembly;
    protected final AtomicInteger curIndex;
    protected final int poolSize;


    public AbstractInvoker(MethodProxy proxy, AbstractListener abstractListener, int poolSize) {
        int threadSize = RuntimeUtil.getAllowThreadSize();
        threadSize = 1000 > threadSize ? threadSize : 1000;

        this.excutor = new ThreadPoolExecutor(poolSize, RuntimeUtil.MAX_POOL_SIZE, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(threadSize), new ShutdownRejected());

        this.abstractListener = abstractListener;
        this.count = new LongAdder();

//        this.handle = proxy.getMethodHandle();
//        this.service = proxy.getService();

        this.methodHandles = proxy.getMethodHandle();
        this.serviceAssembly = proxy.getService();
        this.poolSize = poolSize;
        this.curIndex = new AtomicInteger(0);
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
            LockSupport.parkNanos(NANO_PER_MILLIS);
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
        int index = curIndex.getAndIncrement() % poolSize;
        Object service = serviceAssembly[index];
        MethodHandle handle = methodHandles[index];

        // 开始时间
        long start = System.nanoTime();
        System.out.println(Arrays.toString(argsValue) + "  " + start);
        Timestamp invokeTime = DateUtil.nowTimestamp();
//        Instant start = DateUtil.nowInstant();


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
//        result.setSpend(DateUtil.afterInstant(start));
        result.setSpend((System.nanoTime() - start) / NANO_PER_MILLIS);
        System.out.println(Arrays.toString(argsValue) + "  " + System.nanoTime());
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
            abstractListener.shutdownNow();
            throw new RuntimeException("服务响应过慢，已强制关闭.");
        }
    }
}
