package io.moyada.sharingan.spring.boot.autoconfigure.annotation;

import io.moyada.sharingan.monitor.api.entity.SerializationType;

import java.lang.annotation.*;

/**
 * 方法监视
 * @author xueyikang
 * @since 1.0
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

    /**
     * 数据 domain
     * @return
     */
    String value();

    /**
     * 参数的序列化方式
     * @return
     */
    SerializationType serialization() default SerializationType.VALUE;
}
