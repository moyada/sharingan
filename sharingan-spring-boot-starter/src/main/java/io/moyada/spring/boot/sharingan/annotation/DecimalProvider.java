package io.moyada.spring.boot.sharingan.annotation;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalProvider {

    double min();

    double max();

    int precision() default 3;
}
