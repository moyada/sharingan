package cn.moyada.dubbo.faker.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生成方法调用信息
 * Created by xueyikang on 2017/8/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Exporter {

    /**
     * 参数表达式 ["${appId.type1}", "${appId.type2.param}"]
     */
    String value() default "";

    /**
     * 覆盖原有参数表达式
     * @return
     */
    boolean override() default true;
}
