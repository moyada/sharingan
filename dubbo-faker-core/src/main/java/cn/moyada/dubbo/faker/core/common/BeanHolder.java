package cn.moyada.dubbo.faker.core.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xueyikang
 * @create 2018-03-27 13:09
 */
public class BeanHolder {

    private final ApplicationContext context;

    public BeanHolder(String xmlPath) {
        this.context = new ClassPathXmlApplicationContext(new String[]{xmlPath});
    }

    public Object getBean(Class<?> cls) throws BeansException {
        return context.getBean(cls);
    }
}
