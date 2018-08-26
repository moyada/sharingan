package cn.moyada.dubbo.faker.web.annotation;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @create 2018-04-06 23:41
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ResponseBody
@Inherited
public @interface ApiParam {

    String value();
}
