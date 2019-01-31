package io.moyada.sharingan.infrastructure.util;

import java.util.concurrent.TimeUnit;

/**
 * @author xueyikang
 * @create 2018-04-02 22:33
 */
public class RuntimeUtil {

    public final static int DOUBLE_CORE_SIZE;
    static {
        int cpuCore = Runtime.getRuntime().availableProcessors();
        DOUBLE_CORE_SIZE = cpuCore * 2; // % 8 == 0 ? cpuCore : cpuCore + (8 - cpuCore % 8);
    }

    /**
     * 返回线程数，不过超CPU核心数*2
     * @param expectSize
     * @return
     */
    public static int getActualPoolSize(int expectSize) {
        return expectSize > DOUBLE_CORE_SIZE ? DOUBLE_CORE_SIZE : expectSize;
    }

    public static int getDoubleCore() {
        return DOUBLE_CORE_SIZE;
    }

    public static void gc(long timeout) {
        if (timeout <= 0L) {
            timeout = 100L;
        }

        System.gc();
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
