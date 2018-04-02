package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.domain.LogDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

import static cn.moyada.dubbo.faker.core.common.Constant.NANO_PER_MILLIS;

/**
 * 记录结果监听器
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class LoggingListener extends AbstractListener {
    private static final Logger log = LoggerFactory.getLogger(LoggingListener.class);

    public LoggingListener(String fakerId, int invokeId, long total, FakerManager fakerManager,
                           boolean saveResult, String resultParam) {
        super(1, 5, total, fakerId, invokeId, fakerManager, saveResult, resultParam);
    }

    @Override
    public void record(InvokeFuture result) {
        this.count.increment();
        this.excutor.submit(new InvokerConsumer(result));
    }

    @Override
    public void shutdown() {
        long value;
        for (;;) {
            value = count.longValue();
            LockSupport.parkNanos(1_000 * NANO_PER_MILLIS);
            if(value == count.longValue()) {
                excutor.shutdownNow();
                break;
            }
        }
    }

    class InvokerConsumer implements Runnable {

        private final InvokeFuture future;

        InvokerConsumer(InvokeFuture future) {
            this.future = future;
        }

        @Override
        public void run() {
            LogDO logDO = convert.convertToLog(future);

            log.info("save invoke result: " + convert.getFakerId());

            fakerManager.saveLog(logDO);
        }
    }
}
