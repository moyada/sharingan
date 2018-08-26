package cn.moyada.faker.core.invoke;

import cn.moyada.dubbo.faker.core.exception.UnsupportedParamNumberException;
import cn.moyada.dubbo.faker.core.factory.GroupThreadFactory;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.InvokerInfo;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.model.queue.AbstractQueue;
import cn.moyada.dubbo.faker.core.utils.DateUtil;
import cn.moyada.dubbo.faker.core.utils.ParamUtil;
import cn.moyada.dubbo.faker.core.utils.RuntimeUtil;
import cn.moyada.dubbo.faker.core.utils.ThreadUtil;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

import static cn.moyada.dubbo.faker.core.common.Constant.NANO_PER_MILLIS;

public abstract class AbstractInvoker {

    protected final AbstractQueue<InvokeFuture> queue;

    protected final ExecutorService excutor;
    protected final LongAdder count;

    protected final MethodHandle[] methodHandles;
    protected final Object service;
    private final int questNum;

    public AbstractInvoker(MethodProxy proxy, AbstractQueue<InvokeFuture> queue, InvokerInfo invokerInfo) {
        int poolSize = invokerInfo.getPoolSize();
        int questNum = invokerInfo.getQuestNum();
        int threadSize = RuntimeUtil.getAllowThreadSize() - poolSize;
        threadSize = questNum > threadSize ? threadSize : questNum;
        this.excutor = new ThreadPoolExecutor(poolSize, poolSize, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(threadSize),
                new GroupThreadFactory("invoker", proxy.getFakerId(), Thread.MAX_PRIORITY),
                new ShutdownRejected());

        this.queue = queue;
        this.count = new LongAdder();

        this.methodHandles = proxy.getMethodHandle();
        this.service = proxy.getService();
        this.questNum = questNum;
    }

    /**
     * 调用请求
     * @param argsValue
     */
    public abstract void invoke(Object[] argsValue);
//    public void invoke(Object[] argsValue) {
//        this.excutor.execute(() -> execute(argsValue));
//    }

    /**
     * 等待所有任务完成关闭线程池
     */
    public void shutdownDelay() {
        while (count.intValue() < questNum) {
            LockSupport.parkNanos(1_000 * NANO_PER_MILLIS);
            if(Thread.currentThread().isInterrupted()) {
                Thread.currentThread().interrupt();
            }
        }
        queue.done();
        excutor.shutdown();
    }

    /**
     * 调用接口请求
     * @param argsValue 参数
     */
    protected void execute(Object[] argsValue) {

        MethodHandle handle;
        if(methodHandles.length == 1) {
            handle = methodHandles[0];
        }
        else {
            int index = ThreadUtil.getInnerGroupId();// % poolSize;
            handle = methodHandles[index];
        }

        // 开始时间
        long start = System.nanoTime();
//        System.out.println(Arrays.toString(argsValue) + "  " + start);
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
//            throwable.printStackTrace();
            result = FutureResult.failed(throwable);
        }
//        finally {
//            count.increment();
//        }
        // 完成计算耗时
//        result.setSpend(DateUtil.afterInstant(start));
        result.setSpend((System.nanoTime() - start) / NANO_PER_MILLIS);
        callback(new InvokeFuture(result, invokeTime, ParamUtil.toString(argsValue)));
        count.increment();
    }

    /**
     * 记录调用结果
     * @param result
     */
    protected void callback(InvokeFuture result) {
        queue.offer(result);
    }
//    protected void callback(int producer, InvokeFuture result) {
//        queue.offer(producer, result);
//    }

    public class ShutdownRejected implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("reject!");
            executor.shutdownNow();
            queue.done();
            System.gc();
            throw new RuntimeException("服务响应过慢，已强制关闭.");
        }
    }
}
