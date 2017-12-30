package cn.xueyikang.dubbo.faker.core.consumer;

import cn.xueyikang.dubbo.faker.core.common.Code;
import cn.xueyikang.dubbo.faker.core.manager.FakerManager;
import cn.xueyikang.dubbo.faker.core.model.InvokeFuture;
import cn.xueyikang.dubbo.faker.core.model.LogDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author xueyikang
 * @create 2017-12-30 18:05
 */
public class InvokerConsumer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(InvokerConsumer.class);

    private final Queue<InvokeFuture> queue;
    private final FakerManager fakerManager;
    private final String fakerId;
    private final Integer invokeId;
    private final String name;

    public InvokerConsumer(String name, String fakerId, Integer invokeId, Queue<InvokeFuture> queue, FakerManager fakerManager) {
        this.queue = queue;
        this.fakerManager = fakerManager;
        this.fakerId = fakerId;
        this.invokeId = invokeId;
        this.name = name;
    }

    @Override
    public void run() {
        log.info("InvokerConsumer " + this.name + "start");
        for(;;) {
            if(queue.isEmpty()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            InvokeFuture invokeFuture = queue.poll();
            if(null == invokeFuture) {
                continue;
            }
            log.info(this.name + " save invoke...");
            Instant start = invokeFuture.getStart();
            CompletableFuture<Object> future = invokeFuture.getFuture();

            LogDO logDO = new LogDO();

            future.exceptionally((t)->{
                logDO.setCode(Code.ERROR);
                logDO.setMessage(t.getMessage());
                return null;
            });

            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                continue;
            }

            long millis = Duration.between(start, Instant.now()).toMillis();
            if(null == logDO.getCode()) {
                if (millis > 1000) {
                    logDO.setCode(Code.TIME_OUT);
                } else {
                    logDO.setCode(Code.OK);
                }
            }

            logDO.setFakerId(fakerId);
            logDO.setInvokeId(invokeId);
            logDO.setRealParam(invokeFuture.getRealParam());

            logDO.setInvokeTime(Timestamp.from(start));

            logDO.setSpendTime(millis);
            fakerManager.saveLog(logDO);
        }
    }
}
