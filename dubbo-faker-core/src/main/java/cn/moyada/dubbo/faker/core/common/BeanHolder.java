package cn.moyada.dubbo.faker.core.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.ref.SoftReference;
import java.util.*;

/**
 * 
 * @author xueyikang
 * @create 2018-03-27 13:09
 */
public class BeanHolder {

    private final String xmlPath;

    private Map<Class<?>, List<SoftReference<Object>>> serviceMap;
    private Map<Class<?>, Integer> indexMap;
    private Map<Integer, SoftReference<ApplicationContext>> contextMap;

    public BeanHolder(String xmlPath) {
        this.xmlPath = xmlPath;
        this.serviceMap = new HashMap<>();
        this.indexMap = new HashMap<>();
        this.contextMap = new HashMap<>();
    }

    public Object getBean(int index, Class<?> cls) throws BeansException {
        SoftReference<ApplicationContext> reference = contextMap.get(index);
        ApplicationContext context;
        if(null == reference) {
            context = new ClassPathXmlApplicationContext(new String[]{xmlPath});
            contextMap.put(index, new SoftReference<>(context));
        }
        else {
            context = reference.get();
        }

        if(null == context) {
            context = new ClassPathXmlApplicationContext(new String[]{xmlPath});
            contextMap.put(index, new SoftReference<>(context));
        }
        return context.getBean(cls);
    }

    public void reset() {
        indexMap.keySet().forEach(cls -> indexMap.put(cls, 0));
    }

    public Object getBean(Class<?> cls) throws BeansException {
        List<SoftReference<Object>> softReferences = serviceMap.get(cls);
        Integer index;
        Object service;
        SoftReference<Object> reference;
        if(null == softReferences)  {
            softReferences = new ArrayList<>();
            service = creteBean(cls);
            reference = new SoftReference<>(service);
            softReferences.add(reference);
            serviceMap.put(cls, softReferences);
            indexMap.put(cls, 1);
        }
        else {
            index = indexMap.get(cls);
            if(softReferences.size() <= index) {
                service = creteBean(cls);
                reference = new SoftReference<>(service);
                softReferences.add(reference);
            }
            else {
                reference = softReferences.get(index);
                service = reference.get();
                if (null == service) {
                    service = creteBean(cls);
                    reference = new SoftReference<>(service);
                    softReferences.set(index, reference);
                }
            }
            indexMap.put(cls, index + 1);
        }

        return service;
    }

    public <T> T creteBean(Class<T> cls) throws BeansException {
        return new ClassPathXmlApplicationContext(new String[]{xmlPath}).getBean(cls);
    }
}
