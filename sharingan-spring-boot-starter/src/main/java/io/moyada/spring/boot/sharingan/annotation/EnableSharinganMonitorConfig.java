package io.moyada.spring.boot.sharingan.annotation;

import io.moyada.spring.boot.sharingan.MysqlMonitorConfiguration;
import io.moyada.spring.boot.sharingan.SharinganMonitorAutoConfiguration;
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
@PropertySources(
        {
                @PropertySource(value = "classpath:sharingan.properties", ignoreResourceNotFound = true),
                @PropertySource(value = "classpath:properties/sharingan.properties", ignoreResourceNotFound = true),
                @PropertySource(value = "classpath:config/sharingan.properties", ignoreResourceNotFound = true),
                @PropertySource(value = "classpath:conf/sharingan.properties", ignoreResourceNotFound = true),
                @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
                @PropertySource(value = "classpath:properties/application.properties", ignoreResourceNotFound = true),
                @PropertySource(value = "classpath:config/application.properties", ignoreResourceNotFound = true)
        }
)
@Inherited
@Import( {SharinganMonitorAutoConfiguration.class, MysqlMonitorConfiguration.class} )
public @interface EnableSharinganMonitorConfig {
}
