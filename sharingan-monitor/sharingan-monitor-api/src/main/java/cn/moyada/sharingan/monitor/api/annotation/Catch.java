package cn.moyada.sharingan.monitor.api.annotation;

import cn.moyada.sharingan.monitor.api.entity.SerializationType;

import java.lang.annotation.*;

/**
 * 方法监视
 * @author xueyikang
 * @since 1.0
 **/
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Catch {

    /**
     * domain 信息，替换 {@link Listener#domain()}
     * @return
     */
    String value() default "";

    /**
     * 参数的序列化方式
     * @return
     */
    SerializationType serialization() default SerializationType.VALUE;
}
