package io.moyada.sharingan.spring.boot.starter.annotation;

import java.lang.annotation.*;

/**
 * 注册方法
 * @author xueyikang
 * @since 1.0
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Register {
}
