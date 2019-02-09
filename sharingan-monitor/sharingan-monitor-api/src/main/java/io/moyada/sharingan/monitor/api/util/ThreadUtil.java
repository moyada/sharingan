package io.moyada.sharingan.monitor.api.util;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ThreadUtil {

    /**
     * 增加守护线程处理器
     * @param runnable
     * @return
     */
    public static Thread addShutdownHook(Runnable runnable) {
        Thread thread = new Thread(runnable);
        Runtime.getRuntime().addShutdownHook(thread);
        return thread;
    }
}
