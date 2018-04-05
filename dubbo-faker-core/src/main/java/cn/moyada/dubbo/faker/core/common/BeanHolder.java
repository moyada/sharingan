package cn.moyada.dubbo.faker.core.common;

import org.jboss.netty.handler.codec.serialization.SoftReferenceMap;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

/**
 * 
 * @author xueyikang
 * @create 2018-03-27 13:09
 */
public class BeanHolder {

    private final String xmlPath;

    private SoftReferenceMap<Integer, ApplicationContext> contextMap;

    public BeanHolder(String xmlPath) {
        this.xmlPath = xmlPath;
        this.contextMap = new SoftReferenceMap<>(new HashMap<>());
    }

    public <T> T creteBean(Class<T> cls) throws BeansException {
        return getBean(0, cls);
    }

    public <T> T getBean(int index, Class<T> cls) throws BeansException {
        ApplicationContext context = contextMap.get(index);

        if(null == context) {
            context = new ClassPathXmlApplicationContext(new String[]{xmlPath});
            contextMap.put(index, context);
        }
        return context.getBean(cls);
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
