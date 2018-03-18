package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.common.Code;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.LogDO;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class LoggingListener implements CompletedListener {
    private static final Logger log = LoggerFactory.getLogger(LoggingListener.class);

    private final ExecutorService excutor;
    private final LongAdder count;

    private final String fakerId;
    private final String resultParam;
    private final Integer invokeId;
    private final FakerManager fakerManager;
    private final boolean saveResult;


    public LoggingListener(int poolSie, String fakerId, int invokeId, FakerManager fakerManager,
                           boolean saveResult, String resultParam) {
        this.excutor = Executors.newFixedThreadPool(poolSie > 100 ? 100 : poolSie);
        this.count = new LongAdder();
        this.fakerId = fakerId;
        this.invokeId = invokeId;
        this.fakerManager = fakerManager;
        this.saveResult = saveResult;
        this.resultParam = resultParam;
    }

    public void shutdownDelay() {
        while (count.longValue() != 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.excutor.shutdown();
    }

    @Override
    public void record(InvokeFuture result) {
        this.count.increment();
        this.excutor.submit(new InvokerConsumer(result));
    }

    class InvokerConsumer implements Runnable {

        private final InvokeFuture future;
        InvokerConsumer(InvokeFuture future) {
            this.future = future;
        }

        @Override
        public void run() {
            log.info(Thread.currentThread().getName() + " save invoke...");
            FutureResult result = future.getFuture();

            Object o = result.getResult();
            long spend = result.getSpend();

            LogDO logDO = new LogDO();

            if (result.isSuccess()) {
                if (saveResult) {
                    if (null != resultParam) {
                        o = ReflectUtil.getValue(o, resultParam);
                    }

                    if (null == o) {
                        logDO.setCode(Code.NULL);
                        if (null != resultParam) {
                            logDO.setResult(resultParam + ": null");
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
            logDO.setInvokeTime(future.getInvokeTime());
            logDO.setRealParam(future.getRealParam());

            fakerManager.saveLog(logDO);

            count.decrement();
        }
    }
}
