package cn.moyada.sharingan.monitor.api;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Inherited
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rename {

    String value();
}
