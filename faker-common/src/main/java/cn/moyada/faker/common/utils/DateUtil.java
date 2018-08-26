package cn.moyada.faker.common.utils;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

/**
 * @author xueyikang
 * @create 2018-03-30 17:44
 */
public class DateUtil {

    private static final ZoneId zoneId = ZoneId.of("Asia/Shanghai");

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
}
