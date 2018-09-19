package cn.moyada.sharingan.common.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author xueyikang
 * @create 2018-08-27 14:32
 */
public class TimeUtil {

    private static volatile long currentTimeMillis;

    private static boolean runnable;

    static {
        currentTimeMillis = System.currentTimeMillis();
    }

    public static void doTimekeeping() {
        runnable = true;

        Thread work = new Thread(() -> {
            while (runnable) {
                currentTimeMillis = System.currentTimeMillis();

                try {
                    TimeUnit.MILLISECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        work.setDaemon(true);
        work.setName("time-tick-thread");
        work.start();
    }

    public static void stopTimekeeping() {
        runnable = false;
        currentTimeMillis = 0L;
    }

    public static long currentTimeMillis() {
        return currentTimeMillis;
    }
}
