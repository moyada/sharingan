package cn.moyada.faker.common.utils;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * @author xueyikang
 * @create 2018-08-27 14:32
 */
public class TimeUtil {

    private static Instant INSTANT = Instant.now(Clock.system(ZoneId.of("Asia/Shanghai")));

    public static Timestamp now() {
        return Timestamp.from(INSTANT);
    }
}
