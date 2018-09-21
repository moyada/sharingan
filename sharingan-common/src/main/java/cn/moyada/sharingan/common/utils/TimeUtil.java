package cn.moyada.sharingan.common.utils;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

/**
 * @author xueyikang
 * @create 2018-08-27 14:32
 */
public class TimeUtil {

    private static final ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    private static volatile long currentTimeMillis;

    private static boolean runnable;

    static {
        currentTimeMillis = System.currentTimeMillis();
    }

    /**
     * 返回当前距离Instant多少毫秒
     * @param start
     * @return
     */
    public static long afterInstant(Instant start) {
        return Duration.between(start, nowInstant()).toMillis();
    }

    public static Instant nowInstant() {
        return Instant.now(Clock.system(zoneId));
    }

    public static Timestamp nowTimestamp() {
        return Timestamp.from(nowInstant());
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
