package cn.moyada.dubbo.faker.core.thread;

import cn.moyada.dubbo.faker.core.common.Code;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.LogDO;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

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
            if (queue.isEmpty()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            InvokeFuture invokeFuture = queue.poll();
            if (null == invokeFuture) {
                continue;
            }
            log.info(this.name + " save invoke...");
            FutureResult result = invokeFuture.getFuture();

            Object o = result.getResult();
            long spend = result.getSpend();

            LogDO logDO = new LogDO();

            if (result.isSuccess()) {
                if (this.saveResult) {
                    if (null != this.resultParam) {
                        o = ReflectUtil.getValue(o, this.resultParam);
                    }

                    if (null == o) {
                        logDO.setCode(Code.NULL);
                        if (null != this.resultParam) {
                            logDO.setResult(this.resultParam + ": null");
                        } else {
                            logDO.setResult("null");
                        }
                    } else {
                        if (spend > 1000) {
                            logDO.setCode(Code.TIME_OUT);
                        } else {
                            logDO.setCode(Code.OK);
                        }
                        logDO.setResult(JsonUtil.toGsonJson(o));
                    }
                } else if (spend > 1000) {
                    logDO.setCode(Code.TIME_OUT);
                } else {
                    logDO.setCode(Code.OK);
                }
            } else {
                logDO.setCode(Code.ERROR);
                logDO.setMessage(o.toString());
            }

            logDO.setFakerId(fakerId);
            logDO.setInvokeId(invokeId);
            logDO.setSpendTime(spend);
            logDO.setInvokeTime(invokeFuture.getInvokeTime());
            logDO.setRealParam(invokeFuture.getRealParam());

            fakerManager.saveLog(logDO);
        }
    }
}
