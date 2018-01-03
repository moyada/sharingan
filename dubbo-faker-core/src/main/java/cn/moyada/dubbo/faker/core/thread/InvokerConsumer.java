package cn.moyada.dubbo.faker.core.thread;

import cn.moyada.dubbo.faker.core.common.Code;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.LogDO;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ReflectUtil;
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
    private final boolean saveResult;
    private final String resultParam;

    public InvokerConsumer(String name, String fakerId, Integer invokeId, Queue<InvokeFuture> queue, FakerManager fakerManager,
                           boolean saveResult, String resultParam) {
        this.queue = queue;
        this.fakerManager = fakerManager;
        this.fakerId = fakerId;
        this.invokeId = invokeId;
        this.name = name;
        this.saveResult = saveResult;
        this.resultParam = resultParam;
    }

    @Override
    public void run() {
        log.info("InvokerConsumer " + this.name + "start");
        while (true) {
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

            Object o;
            try {
                o = future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("save invoke error " + e);
                continue;
            }

            long millis = Duration.between(start, Instant.now()).toMillis();

            LogDO logDO = new LogDO();
            if(o instanceof Throwable) {
                logDO.setCode(Code.ERROR);
                logDO.setMessage(Throwable.class.cast(o).getMessage());
            }
            else {
                if(this.saveResult) {
                    if(null != this.resultParam) {
                        o = ReflectUtil.getValue(o, this.resultParam);
                    }
                    if(null == o) {
                        logDO.setCode(Code.NULL);
                        if(null != this.resultParam) {
                            logDO.setResult(this.resultParam + ": null");
                        }
                        else {
                            logDO.setResult("null");
                        }
                    }
                    else {
                        logDO.setCode(Code.OK);
                        logDO.setResult(JsonUtil.toGsonJson(o));
                    }
                }
                else {
                    logDO.setCode(Code.OK);
                }
                // TODO: 2017/12/31 counting spend time
//                if (millis > 1000) {
//                    logDO.setCode(Code.TIME_OUT);
//                } else {
//                    logDO.setCode(Code.OK);
//                }
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
