package cn.moyada.dubbo.faker.core.common;

import com.alibaba.dubbo.config.spring.ServiceBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author xueyikang
 * @create 2018-03-27 13:09
 */
public class BeanHolder {

    private final ApplicationContext context;

    public BeanHolder(String xmlPath) {
        ApplicationContext springContext = ServiceBean.getSpringContext();
        if(null == springContext) {
            this.context = new ClassPathXmlApplicationContext(new String[]{xmlPath});
        }
        else {
            this.context = springContext;
        }
    }

    public Object getBean(Class<?> cls) throws BeansException {
        return context.getBean(cls);
    }
}
