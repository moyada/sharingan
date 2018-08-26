package cn.moyada.faker.core.listener;

import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.faker.common.model.queue.AbstractQueue;
import cn.moyada.faker.core.QuestInfo;
import cn.moyada.faker.manager.domain.LogDO;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 批量保存结果监听器
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class BatchLoggingListener extends AbstractListener {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BatchLoggingListener.class);

    private final AtomicReference<List<LogDO>> list;

    private volatile boolean stop;

    public BatchLoggingListener(String fakerId, QuestInfo invokerInfo,
                                AbstractQueue<InvokeFuture> queue) {
        super(fakerId, invokerInfo, queue);
        int num = invokerInfo.getQuestNum() / 1000;
        num = num == 0 ? 1: num;
        this.list = new AtomicReference<>(new ArrayList<>(1000 * num));
        this.stop = false;
    }

    private void stopLogger() {
        for(;;) {
            if(list.get().isEmpty()) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        stop = true;
    }

    @Override
    public void startListener() {
        new Thread(new Converter()).start();
        new Thread(new Logger()).start();
    }

    class Converter implements Runnable {

        @Override
        public void run() {
            LogDO logDO;
            for (;;) {
                InvokeFuture future = futureQueue.poll();
                if(null == future) {
                    if(futureQueue.isDone()) {
                        break;
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

                logDO = convert.convertToLog(future);
                list.get().add(logDO);

            }
            stopLogger();
            log.info("logging shutdown: " + convert.getFakerId());
        }
    }

    class Logger implements Runnable {

        private List<LogDO> saveList = new ArrayList<>(1000);

        @Override
        public void run() {
            for (;;) {
                // 每秒批量保存一次
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                saveList = list.getAndSet(saveList);
                save(saveList);

                if(stop) {
                    break;
                }
            }
        }

        private void save(List<LogDO> logDOs) {
            if(logDOs.isEmpty()) {
                return;
            }
            int size = logDOs.size();
            log.info("batch save invoke result. fakerId: " + convert.getFakerId() + ", size: " + size);
            fakerManager.saveLog(logDOs);
            logDOs.clear();
        }
    }
}
