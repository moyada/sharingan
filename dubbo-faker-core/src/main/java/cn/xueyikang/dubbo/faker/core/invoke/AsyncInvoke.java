package cn.xueyikang.dubbo.faker.core.invoke;

import cn.xueyikang.dubbo.faker.core.common.Code;
import cn.xueyikang.dubbo.faker.core.manager.FakerManager;
import cn.xueyikang.dubbo.faker.core.model.LogDO;

import java.lang.invoke.MethodHandle;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncInvoke extends AbstractInvoke implements AutoCloseable {

    private final ExecutorService excutor;

    private FakerManager fakerManager;

    public AsyncInvoke(int poolSize, FakerManager fakerManager) {
        this.excutor = Executors.newFixedThreadPool(poolSize);
        this.fakerManager = fakerManager;
    }

    @Override
    public void invoke(String fakerId, MethodHandle handle, Object[] argsValue) {
        LogDO logDO = new LogDO();
        logDO.setFakerId(fakerId);

        Instant start = Instant.now();

        logDO.setInvokeTime(Timestamp.from(start));

        CompletableFuture<Object> resultFuture = CompletableFuture.supplyAsync(()->{
            try {
                return handle.invoke(argsValue);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        }, this.excutor);
        resultFuture.exceptionally((t)->{
            logDO.setCode(Code.ERROR);
            logDO.setMessage(t.getMessage());
            return null;
        });

        try {
            resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        long millis = Duration.between(start, Instant.now()).toMillis();
        if(millis > 1000) {
            logDO.setCode(Code.TIME_OUT);
        }
        else {
            logDO.setCode(Code.OK);
        }
        logDO.setSpendTime(millis);
        fakerManager.saveLog(logDO);
    }

    public void destroy() {
        this.excutor.shutdown();
    }

    @Override
    public void close() throws Exception {
        this.destroy();
    }
}
