package io.moyada.sharingan.spring.boot.autoconfigure.annotation;

import java.lang.annotation.*;

/**
 * 监视参数名称修改
 * @author xueyikang
 * @since 1.0
 **/
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rename {

    String value();
}
