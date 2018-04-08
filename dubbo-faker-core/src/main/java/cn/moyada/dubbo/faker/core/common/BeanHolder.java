package cn.moyada.dubbo.faker.core.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author xueyikang
 * @create 2018-03-27 13:09
 */
public class BeanHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> cls) throws BeansException {
        T bean = applicationContext.getBean(cls);
        if(null == bean) {
            throw new NullPointerException(cls.getName() + "can not find any bean.");
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanHolder.applicationContext = applicationContext;
    }

//    public Object getBean(Class<?> cls) throws BeansException {
//        List<SoftReference<Object>> softReferences = serviceMap.get(cls);
//        Integer index;
//        Object service;
//        SoftReference<Object> reference;
//        if(null == softReferences)  {
//            softReferences = new ArrayList<>();
//            service = creteBean(cls);
//            reference = new SoftReference<>(service);
//            softReferences.add(reference);
//            serviceMap.put(cls, softReferences);
//            indexMap.put(cls, 1);
//        }
//        else {
//            index = indexMap.get(cls);
//            if(softReferences.size() <= index) {
//                service = creteBean(cls);
//                reference = new SoftReference<>(service);
//                softReferences.add(reference);
//            }
//            else {
//                reference = softReferences.get(index);
//                service = reference.get();
//                if (null == service) {
//                    service = creteBean(cls);
//                    reference = new SoftReference<>(service);
//                    softReferences.set(index, reference);
//                }
//            }
//            indexMap.put(cls, index + 1);
//        }
//
//        return service;
//    }
}
