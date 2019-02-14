package io.moyada.sharingan.spring.boot.autoconfigure.annotation;

import io.moyada.sharingan.monitor.api.entity.Protocol;

import java.lang.annotation.*;

/**
 * 监视标记
 * @author xueyikang
 * @since 0.0.1
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {

    String name() default "";

    Class value();

    /**
     * 服务提供的协议
     * @return
     */
    Protocol protocol();
}
