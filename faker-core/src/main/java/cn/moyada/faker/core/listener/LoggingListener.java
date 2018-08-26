package cn.moyada.faker.core.listener;


import cn.moyada.dubbo.faker.core.model.InvokerInfo;
import cn.moyada.dubbo.faker.core.model.domain.LogDO;
import cn.moyada.faker.common.model.InvokeFuture;
import cn.moyada.faker.common.model.queue.AbstractQueue;
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

    public LoggingListener(String fakerId, InvokerInfo invokerInfo, AbstractQueue<InvokeFuture> queue) {
        super(fakerId, invokerInfo, queue);
    }

    @Override
    public void startListener() {
        new Thread(new Consumer()).start();
    }

    class Consumer implements Runnable {

        @Override
        public void run() {
            for (;;) {
                InvokeFuture future = futureQueue.poll();
                if(null == future) {
                    if(futureQueue.isDone()) {
                        break;
                    }
                    LockSupport.parkNanos(1_000 * NANO_PER_MILLIS);
                    continue;
                }

                LogDO logDO = convert.convertToLog(future);

                log.info("save invoke result: " + convert.getFakerId());

                fakerManager.saveLog(logDO);
            }
            log.info("logging shutdown: " + convert.getFakerId());
        }
    }
}
