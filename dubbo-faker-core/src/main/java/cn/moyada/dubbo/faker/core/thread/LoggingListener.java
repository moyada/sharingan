package cn.moyada.dubbo.faker.core.thread;

import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.InvokeFuture;

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

    public LoggingListener(int poolSie) {
        this.poolSie = poolSie > 100 ? 100 : poolSie;
        this.excutor = Executors.newFixedThreadPool(this.poolSie);
    }

    public void run(String fakerId, int invokeId, Queue<InvokeFuture> queue,
                    FakerManager fakerManager, boolean saveResult, String resultParam) {
        for (int index = 0; index < this.poolSie; index++) {
            this.excutor.submit(new InvokerConsumer("t-"+index, fakerId, invokeId, queue, fakerManager, saveResult, resultParam));
        }
    }


    public void shutdown() {
        this.excutor.shutdown();
    }
}
