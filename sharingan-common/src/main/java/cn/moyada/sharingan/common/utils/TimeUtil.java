package cn.moyada.sharingan.common.utils;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具
 * @author xueyikang
 * @create 2018-08-27 14:32
 */
public class TimeUtil {

    // 时区
    private static final ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    // 时钟毫秒
    private static volatile long currentTimeMillis;

    // 时钟开关
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

    /**
     * 获取当前时间实例
     * @return
     */
    public static Instant nowInstant() {
        return Instant.now(Clock.system(zoneId));
    }

    /**
     * 获取当前时间
     * @return
     */
    public static Timestamp nowTimestamp() {
        return Timestamp.from(nowInstant());
    }

    /**
     * 开启时钟线程，每1毫秒更新
     */
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

    /**
     * 暂时时钟线程
     */
    public static void stopTimekeeping() {
        runnable = false;
        currentTimeMillis = 0L;
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static long currentTimeMillis() {
        return currentTimeMillis;
    }
}
