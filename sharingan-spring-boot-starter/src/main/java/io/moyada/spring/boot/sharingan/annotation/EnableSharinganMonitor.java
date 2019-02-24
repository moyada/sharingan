package io.moyada.spring.boot.sharingan.annotation;

import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@EnableSharinganMonitorConfig
@EnableSharinganMonitorScan
public @interface EnableSharinganMonitor {

    String ENABLE_PREFIX = MonitorConfig.PREFIX + ".enable";

    @AliasFor(
            annotation = EnableSharinganMonitorScan.class,
            attribute = "value"
    )
    String[] value() default {};

    @AliasFor(
            annotation = EnableSharinganMonitorScan.class,
            attribute = "basePackages"
    )
    String[] basePackages() default {};

    @AliasFor(
            annotation = EnableSharinganMonitorScan.class,
            attribute = "basePackageClasses"
    )
    Class<?>[] basePackageClasses() default {};
}
