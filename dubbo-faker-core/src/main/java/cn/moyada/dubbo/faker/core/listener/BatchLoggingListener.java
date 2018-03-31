package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.common.Switch;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;
import cn.moyada.dubbo.faker.core.model.domain.LogDO;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * 结果合并保存监听器
 * @author xueyikang
 * @create 2018-03-18 17:32
 */
public class BatchLoggingListener extends AbstractListener {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BatchLoggingListener.class);

    private final List<LogDO> list1;
    private final List<LogDO> list2;

    private final Switch lock;
    private volatile boolean stop;

    public BatchLoggingListener(String fakerId, int invokeId, long total, FakerManager fakerManager,
                                boolean saveResult, String resultParam) {
        super(2, 5, total, fakerId, invokeId, fakerManager, saveResult, resultParam);
        this.list1 = new ArrayList<>(500);
        this.list2 = new ArrayList<>(500);
        this.lock = new Switch(true);
        this.stop = false;
        new Thread(new Logger()).start();
    }

    public void shutdownDelay() {
        super.shutdownDelay();
        this.stop = true;
    }

    @Override
    public void record(InvokeFuture result) {
        excutor.submit(new Converter(result));
    }

    @Override
    public void shutdown() {
        for (;;) {
            LockSupport.parkNanos(1_000_000_000L);
            if(list1.isEmpty() && list2.isEmpty()) {
                excutor.shutdownNow();
                break;
            }
        }
    }

    class Converter implements Runnable {

        private final InvokeFuture result;

        Converter(InvokeFuture result) {
            this.result = result;
        }

        @Override
        public void run() {
            count.increment();
            if(lock.isOpen()) {
                list1.add(convert.convertToLog(result));
            }
            else {
                list2.add(convert.convertToLog(result));
            }
        }
    }

    class Logger implements Runnable {

        @Override
        public void run() {
            for (;;) {
                LockSupport.parkNanos(1_000_000_000L);
                if (lock.close()) {
                    save(list1);
                }
                else if (lock.open()) {
                    save(list2);
                }

                if(stop) {
                    break;
                }
            }
        }

        private void save(List<LogDO> logDOs) {
            if(logDOs.isEmpty()) {
                return;
            }
            log.info("batch save invoke result: " + convert.getFakerId() + ", size: " + logDOs.size());
            fakerManager.saveLog(logDOs);
            logDOs.clear();
        }
    }
}
