package cn.moyada.dubbo.faker.core.listener;

import cn.moyada.dubbo.faker.core.convert.LoggingConvert;
import cn.moyada.dubbo.faker.core.manager.FakerManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

/**
 * @author xueyikang
 * @create 2018-03-18 17:12
 */
public abstract class AbstractListener implements Listener {

    protected final ExecutorService excutor;
    protected final LongAdder count;

    protected final LoggingConvert convert;

    protected final FakerManager fakerManager;

    protected final long total;

    protected AbstractListener(int poolSize, int maxPoolSize, long total, String fakerId, int invokeId, FakerManager fakerManager,
                               boolean saveResult, String resultParam) {
        this.excutor = new ThreadPoolExecutor(poolSize, maxPoolSize, 5L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        this.count = new LongAdder();
        this.fakerManager = fakerManager;
        this.convert = new LoggingConvert(fakerId, invokeId, saveResult, resultParam);
        this.total = total;
    }

    public void shutdownDelay() {
        // 是否全部记录完了
        while (count.longValue() != total) {
            LockSupport.parkNanos(1_000_000L);
        }
        this.excutor.shutdown();
    }
}
