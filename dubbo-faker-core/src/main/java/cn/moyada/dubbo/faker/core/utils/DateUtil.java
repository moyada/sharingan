package cn.moyada.dubbo.faker.core.utils;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * @author xueyikang
 * @create 2018-03-30 17:44
 */
public class DateUtil {

    private static final ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    public static Timestamp now() {
        return Timestamp.from(Instant.now(Clock.system(zoneId)));
    }
}
