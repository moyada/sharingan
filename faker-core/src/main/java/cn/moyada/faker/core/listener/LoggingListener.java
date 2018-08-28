package cn.moyada.faker.core.listener;


import cn.moyada.faker.common.constant.TimeConstant;
import cn.moyada.faker.core.queue.AbstractQueue;
import cn.moyada.faker.core.task.TaskEnvironment;
import cn.moyada.faker.manager.domain.LogDO;
import cn.moyada.faker.rpc.api.invoke.InvokeCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;


/**
 * 记录结果监听器
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class LoggingListener extends AbstractListener implements InvokeCallback {
    private static final Logger log = LoggerFactory.getLogger(LoggingListener.class);

    public LoggingListener(TaskEnvironment env, AbstractQueue<LogDO> queue) {
        super(env, queue);
    }

    @Override
    public void startListener() {
        new Thread(new Consumer()).start();
    }

    class Consumer implements Runnable {

        @Override
        public void run() {
            LogDO logDO;

            for (;;) {
                logDO = queue.poll();
                if(null == logDO) {
                    if(queue.isDone()) {
                        break;
                    }
                    LockSupport.parkNanos(1_000 * TimeConstant.NANO_PER_MILLIS);
                    continue;
                }


                log.info("save invoke result: " + recordHandler.getFakerId());

                fakerManager.saveLog(logDO);
            }
            log.info("logging shutdown: " + recordHandler.getFakerId());
        }
    }
}
