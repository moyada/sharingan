package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.common.Code;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.LogDO;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ReflectUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

/**
 * 调用结果监听器
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class LoggingListener implements CompletedListener {

    private final ExecutorService excutor;
    private final LongAdder count;

    private final String fakerId;
    private final String resultParam;
    private final Integer invokeId;
    private final FakerManager fakerManager;
    private final boolean saveResult;


    public LoggingListener(String fakerId, int invokeId, FakerManager fakerManager,
                           boolean saveResult, String resultParam) {
        this.excutor = new ThreadPoolExecutor(1, 3, 2L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        this.count = new LongAdder();
        this.fakerId = fakerId;
        this.invokeId = invokeId;
        this.fakerManager = fakerManager;
        this.saveResult = saveResult;
        this.resultParam = resultParam;
    }

    public void shutdownDelay() {
        // 是否全部记录完了
        while (count.longValue() != 0) {
            LockSupport.parkNanos(10000);
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
            FutureResult result = future.getFuture();
            long spend = result.getSpend();
            LogDO logDO = new LogDO();

            if (result.isSuccess()) {
                if (saveResult) {
                    // 是否保存结果
                    if (null == resultParam) {
                        String invokeResult = JsonUtil.toJson(result.getResult());
                        if (null == invokeResult) {
                            logDO.setCode(Code.NULL);
                            logDO.setResult("null");
                        }
                        else {
                            logDO.setCode(spend > 1000 ? Code.TIME_OUT : Code.OK);
                            logDO.setResult(invokeResult);
                        }
                    }
                    else {
                        // 保存结果的单个参数
                        Object param = ReflectUtil.getValue(result.getResult(), resultParam);
                        if (null == param) {
                            logDO.setCode(Code.NULL);
                            logDO.setResult(resultParam + ": null");
                        }
                        else {
                            logDO.setCode(spend > 1000 ? Code.TIME_OUT : Code.OK);
                            logDO.setResult(param.toString());
                        }
                    }
                }
                else {
                    logDO.setCode(spend > 1000 ? Code.TIME_OUT : Code.OK);
                }
            }
            else {
                logDO.setCode(Code.ERROR);
                logDO.setMessage(result.getResult().toString());
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
