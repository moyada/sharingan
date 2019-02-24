package io.moyada.spring.boot.sharingan.annotation;

import io.moyada.sharingan.monitor.api.entity.ContentType;
import io.moyada.sharingan.monitor.api.entity.HttpType;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethod {

    String value() default "";

    HttpType type() default HttpType.GET;

    ContentType contentType() default ContentType.APPLICATION_FORM_URLENCODED;

    String[] param() default {};

    String[] header() default {};
}
