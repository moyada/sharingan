package io.moyada.spring.boot.sharingan.annotation;

import java.lang.annotation.*;

/**
 * 排除监视参数
 * @author xueyikang
 * @since 1.0
 **/
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclusive {
}
