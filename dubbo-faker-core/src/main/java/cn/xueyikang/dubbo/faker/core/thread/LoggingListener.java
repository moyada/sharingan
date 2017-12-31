package cn.xueyikang.dubbo.faker.core.thread;

import cn.xueyikang.dubbo.faker.core.manager.FakerManager;
import cn.xueyikang.dubbo.faker.core.model.InvokeFuture;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xueyikang
 * @create 2017-12-31 16:39
 */
public class LoggingListener {

    private final ExecutorService excutor;

    private final int poolSie;
    private final long timeout;

    public LoggingListener(int poolSie, long timeout) {
        this.excutor = Executors.newFixedThreadPool(poolSie > 100 ? 100 : poolSie);
        this.poolSie = poolSie;
        this.timeout = timeout;
    }

    public void run(String fakerId, int invokeId, Queue<InvokeFuture> queue,
                    FakerManager fakerManager, boolean saveResult, String resultParam) {
        for (int index = 0; index < this.poolSie; index++) {
            this.excutor.submit(new InvokerConsumer("t-"+index, fakerId, invokeId, queue, fakerManager, saveResult, resultParam));
            try {
                Thread.sleep(this.timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void shutdown() {
        this.excutor.shutdown();
    }
}
