package io.moyada.sharingan.spring.boot.autoconfigure.annotation;

import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.MonitorRegistrar;
import io.moyada.sharingan.spring.boot.autoconfigure.MysqlMonitorConfiguration;
import io.moyada.sharingan.spring.boot.autoconfigure.MonitorScannerProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@PropertySources(
        {
                @PropertySource(value = "classpath:sharingan.properties", ignoreResourceNotFound = true),
                @PropertySource(value = "classpath:properties/sharingan.properties", ignoreResourceNotFound = true),
                @PropertySource(value = "classpath:config/sharingan.properties", ignoreResourceNotFound = true),
                @PropertySource(value = "classpath:conf/sharingan.properties", ignoreResourceNotFound = true)
        }
)
@Import({MonitorScannerProcessor.class, MysqlMonitorConfiguration.class, MonitorRegistrar.class})
//@Import({SharinganMonitorConfiguration.class, MysqlMonitorConfiguration.class, MonitorRegistrar.class})
public @interface EnableMonitor {

    String ENABLE_PREFIX = MonitorConfig.PREFIX + ".enable";

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
     * {@code @EnableMonitor("org.my.pkg")} instead of {@code @EnableMonitor(basePackages="org.my.pkg")}.
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated components. {@link #value()} is an alias for (and mutually exclusive with) this
     * attribute. Use {@link #basePackageClasses()} for a type-safe alternative to String-based package names.
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components. The
     * package of each class specified will be scanned. Consider creating a special no-op marker class or interface in
     * each package that serves no purpose other than being referenced by this attribute.
     */
    Class<?>[] basePackageClasses() default {};
}
