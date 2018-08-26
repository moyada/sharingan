package cn.moyada.dubbo.faker.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 获取请求参数
 * Created by xueyikang on 2017/8/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Fetch {

    /**
     * 拦截参数归类
     */
    String value();
}
