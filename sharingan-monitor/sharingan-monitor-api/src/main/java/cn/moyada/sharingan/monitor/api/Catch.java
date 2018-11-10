package cn.moyada.sharingan.monitor.api;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Catch {

    String value() default "";
}
