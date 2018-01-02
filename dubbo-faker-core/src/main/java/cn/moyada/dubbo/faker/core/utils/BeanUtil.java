package cn.moyada.dubbo.faker.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xueyikang on 2017/8/6.
 */
public class BeanUtil {

    public static Object getBean(ApplicationContext context, String className) throws BeansException {
        return context.getBean(className);
    }

    public static Object getBean(ApplicationContext context, Class<?> cls) throws BeansException {
        return context.getBean(cls);
    }

    public static Object getBean(ClassPathXmlApplicationContext context, String className) throws BeansException {
        return context.getBean(className);
    }

    public static Object getBean(ClassPathXmlApplicationContext context, Class<?> cls) throws BeansException {
        return context.getBean(cls);
    }
}
