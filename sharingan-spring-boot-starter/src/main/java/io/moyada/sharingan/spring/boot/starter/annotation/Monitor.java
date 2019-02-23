package io.moyada.sharingan.spring.boot.starter.annotation;

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

    /**
     * 服务名
     * @return
     */
    String name() default "";

    /**
     * 服务接口
     * @return
     */
    Class value() default Object.class;

    /**
     * 服务提供的协议
     * @return
     */
    Protocol protocol();
}
