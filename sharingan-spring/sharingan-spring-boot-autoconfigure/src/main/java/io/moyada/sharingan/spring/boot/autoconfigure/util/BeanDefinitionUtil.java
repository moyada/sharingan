package io.moyada.sharingan.spring.boot.autoconfigure.util;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class BeanDefinitionUtil {

    /**
     * 获取类信息
     * @param definition
     * @return
     */
    public static Class getClass(AbstractBeanDefinition definition) {
        Class<?> beanClass;
        if (definition.hasBeanClass()) {
            beanClass = definition.getBeanClass();
        } else {
            String beanClassName = definition.getBeanClassName();
            if (null == beanClassName) {
                return null;
            }
            try {
                beanClass = Class.forName(beanClassName);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return beanClass;
    }

    /**
     * 获取包扫描路径根集合
     * @param applicationContext
     * @return
     */
    public static List<String> getBasePackages(ApplicationContext applicationContext) {
        List<String> backPackages = new ArrayList<>();

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ComponentScan.class);
        if (beans.isEmpty()) {
            return null;
        }
        Class<?>[] classes;
        String[] packages;
        for (Object instance : beans.values()) {
            Set<ComponentScan> scans = AnnotatedElementUtils.getMergedRepeatableAnnotations(instance.getClass(), ComponentScan.class);
            for (ComponentScan scan : scans) {
                classes = scan.basePackageClasses();
                packages = scan.basePackages();

                if (classes.length == 0 && packages.length == 0) {
                    backPackages.add(instance.getClass().getPackage().getName());
                    continue;
                }

                if (classes.length != 0) {
                    for (Class<?> clazz : classes) {
                        backPackages.add(clazz.getPackage().getName());
                    }
                }

                if (packages.length != 0) {
                    backPackages.addAll(Arrays.asList(packages));
                }
            }
        }

        if (backPackages.isEmpty()) {
            return null;
        }

        return removeSubPackage(backPackages);
    }

    private static List<String> removeSubPackage(List<String> backPackages) {
        Collections.sort(backPackages);

        List<String> newBackPackages = new ArrayList<>();
        String base = backPackages.get(0);
        newBackPackages.add(base);
        for (String backPackage : backPackages) {
            if (!backPackage.contains(base)) {
                newBackPackages.add(backPackage);
                base = backPackage;
            }
        }

        return newBackPackages;
    }
}
