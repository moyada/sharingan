package cn.moyada.dubbo.faker.core.invoke;

import cn.moyada.dubbo.faker.core.listener.CompletedListener;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

/**
 * @author xueyikang
 * @create 2018-03-27 12:19
 */
public class DefaultInvoker extends AbstractInvoker {

    public DefaultInvoker(MethodHandle handle, Object service, CompletedListener completedListener, int poolSize) {
        super(handle, service, completedListener, poolSize);
    }

    @Override
    public void invoke(Object[] argsValue) {
        super.count.increment();
        this.excutor.submit(() -> {
            // 开始时间
            Timestamp invokeTime = Timestamp.from(Instant.now());
            FutureResult result;

            long start = System.nanoTime();
            try {
                result = FutureResult.success(super.execute(argsValue));
            } catch (Throwable e) {
                result = FutureResult.failed(e.getMessage());
            }
            // 完成计算耗时
            result.setSpend((System.nanoTime() - start) / 1000_000);
            super.callback(new InvokeFuture(result, invokeTime, Arrays.toString(argsValue)));
            super.count.decrement();
        });
    }
}
