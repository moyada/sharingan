package cn.moyada.dubbo.faker.filter.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import cn.moyada.dubbo.faker.filter.domain.RealParamDO;
import cn.moyada.dubbo.faker.filter.utils.JsonUtil;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志打印
 * @author xueyikang
 * @create 2018-04-13 11:19
 */
public class LoggerBuilder {

    private static final String LOG_DIR = "faker/";

    private static final Map<String,Logger> container = new HashMap<>();

    public static Logger getLogger(String name) {
        Logger logger = container.get(name);
        if(logger != null) {
            return logger;
        }
        synchronized (LoggerBuilder.class) {
            logger = container.get(name);
            if(logger != null) {
                return logger;
            }
            logger = build(name);
            container.put(name,logger);
        }
        return logger;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Logger build(String name) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger("faker-" + name);
        logger.setAdditive(false);
        RollingFileAppender appender = new RollingFileAppender();
        appender.setContext(context);
        appender.setName(name);
        appender.setFile(OptionHelper.substVars("${LOG_HOME}/" + LOG_DIR + name + ".log",context));
        appender.setAppend(true);
        appender.setPrudent(false);
        SizeAndTimeBasedRollingPolicy policy = new SizeAndTimeBasedRollingPolicy();
        String fp = OptionHelper.substVars("${LOG_HOME}/" + LOG_DIR + name + ".log.%d{yyyy-MM-dd}.%i",context);

        policy.setMaxFileSize(FileSize.valueOf("128MB"));
        policy.setFileNamePattern(fp);
        policy.setMaxHistory(30);
        policy.setTotalSizeCap(FileSize.valueOf("32GB"));
        policy.setParent(appender);
        policy.setContext(context);
        policy.start();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} msg:%m%n");
//        encoder.setPattern("%d{yyyy-MM-dd/HH:mm:ss.SSS}|%X{localIp}|[%t] %-5level %logger{50} - %m%n");
        encoder.start();

        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        appender.start();
        logger.addAppender(appender);
        return logger;
    }

    public static void main(String[] args) {
        RealParamDO paramDO = new RealParamDO();
        paramDO.setAppId(12);
        paramDO.setParamValue("34retrgf");
        paramDO.setType("test");

        Logger test = build("test");
        test.info("test");
        test.info(JsonUtil.toJson(paramDO));

        test = build("car-model");
        test.info("test");
    }
}
