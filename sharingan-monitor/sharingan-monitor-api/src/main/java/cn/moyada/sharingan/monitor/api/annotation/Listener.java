package cn.moyada.sharingan.monitor.api.annotation;

import cn.moyada.sharingan.monitor.api.Protocol;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

    String domain();

    Protocol protocol();
}
