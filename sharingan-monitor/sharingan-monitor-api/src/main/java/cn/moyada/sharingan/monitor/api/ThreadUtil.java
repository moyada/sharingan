package cn.moyada.sharingan.monitor.api;

import cn.moyada.sharingan.monitor.api.handler.InvocationHandler;
import cn.moyada.sharingan.monitor.api.processor.AbstractInvocationWorker;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ThreadUtil {

    public static Thread addShutdownHook(Runnable runnable) {
        Thread thread = new Thread(runnable);
        Runtime.getRuntime().addShutdownHook(thread);
        return thread;
    }

    /**
     * 增加守护线程处理器
     * @param worker
     * @param <T>
     * @return
     */
    public static <T> Thread addShutdownHook(AbstractInvocationWorker<T> worker) {
        ShutdownHookThread<T> shutdownHookThread = new ShutdownHookThread<>(worker.getHandler(), worker.getData());
        Runtime.getRuntime().addShutdownHook(shutdownHookThread);
        return shutdownHookThread;
    }

    /**
     * 持久化数据处理器线程
     * @param <E>
     */
    static class ShutdownHookThread<E> extends Thread {

        private InvocationHandler<Collection<E>> handler;
        private Collection<E> data;

        ShutdownHookThread(InvocationHandler<Collection<E>> handler,
                           Collection<E> data) {
            this.handler = handler;
            this.data = data;
        }

        @Override
        public void run() {
            if (data.isEmpty()) {
                return;
            }

            handler.handle(data);
        }
    }
}
