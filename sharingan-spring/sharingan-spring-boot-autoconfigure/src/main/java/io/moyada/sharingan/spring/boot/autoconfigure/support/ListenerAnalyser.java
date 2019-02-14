package io.moyada.sharingan.spring.boot.autoconfigure.support;

import io.moyada.sharingan.spring.boot.autoconfigure.annotation.*;
import io.moyada.sharingan.spring.boot.autoconfigure.util.VariableUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ListenerAnalyser {

    /**
     * 获取标记方法
     * @param targetClass
     * @return
     */
    public static ListenerInfo getListenerInfo(Class<?> targetClass) {
        if (!targetClass.isAnnotationPresent(Monitor.class)) {
            return null;
        }

        List<ListenerMethod> listenerMethod = getListenerMethod(targetClass);
        if (listenerMethod.isEmpty()) {
            return null;
        }
        Monitor annotation = targetClass.getAnnotation(Monitor.class);
        Class classType = annotation.value();
        String name = annotation.name().isEmpty() ? classType.getSimpleName() : annotation.name();
        return new ListenerInfo(name, classType, annotation.protocol(), listenerMethod);
    }

    /**
     * 获取含有注解的类、父类、接口
     * @param clazz
     * @return
     */
    public static List<Class<?>> getExtraClass(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Class<?>> classes = new ArrayList<>();
        classes.addAll(getAllInterface(clazz, annotation));
        classes.addAll(getSuperClass(clazz, annotation));
        return classes;
    }

    /**
     * 获取注解接口
     * @param clazz
     */
    private static List<Class<?>> getAllInterface(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Class<?>> anInterface = getInterface(clazz, annotation);

        List<Class<?>> superClass = getSuperClass(clazz, annotation);
        for (Class<?> cls : superClass) {
            anInterface.addAll(getInterface(cls, annotation));
        }
        return anInterface;
    }

    private static List<Class<?>> getInterface(Class<?> clazz, Class<? extends Annotation> annotation) {
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            return Collections.emptyList();
        }

        List<Class<?>> annoInter = new ArrayList<>();
        for (Class<?> inter : interfaces) {
            if (inter.isAnnotationPresent(annotation)) {
                annoInter.add(inter);
            }
            annoInter.addAll(getInterface(inter, annotation));
        }
        return annoInter;
    }

    /**
     * 获取注解父类
     * @param clazz
     * @param annotation
     */
    private static List<Class<?>> getSuperClass(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Class<?>> annoClass = new ArrayList<>();

        Class<?> superclass = clazz.getSuperclass();
        while (superclass != Object.class) {
            if (superclass.isAnnotationPresent(annotation)) {
                annoClass.add(superclass);
            }
            superclass = superclass.getSuperclass();
        }

        return annoClass;
    }

    /**
     * 获取方法代理信息，排除静态、私有、不可变方法
     * @param clazz
     */
    private static List<ListenerMethod> getListenerMethod(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        List<ListenerMethod> listenerMethods = new ArrayList<>(methods.length);

        for (Method method : methods) {
            if (!needListener(method)) {
                continue;
            }

            ListenerMethod listenerMethod = new ListenerMethod(method);
            if (method.isAnnotationPresent(Register.class)) {
                listenerMethod.setNeesRegister();
                listenerMethod.setHttpData(method.getAnnotation(HttpMethod.class));
            }
            listenerMethods.add(listenerMethod);

            Listener value = method.getAnnotation(Listener.class);
            if (null == value) {
                continue;
            }
            List<ProxyField> proxyFields = getListenerInfo(clazz, method);
            if (null == proxyFields) {
                continue;
            }

            listenerMethod.setDomain(value.value());
            listenerMethod.setSerializationType(value.serialization().getDeclaringClass().getName()
                    + "." + value.serialization().name());

            listenerMethod.setProxyParams(proxyFields);
        }

        return listenerMethods;
    }

    private static boolean needListener(Method method) {
        if (method.getParameterCount() == 0) {
            return false;
        }
        if (!method.isAnnotationPresent(Listener.class) && !method.isAnnotationPresent(Register.class)) {
            return false;
        }
        return (method.getModifiers() & (Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)) == 0;
    }

    /**
     * 获取方法参数代理信息
     * @param clazz
     * @param method
     * @return
     */
    private static List<ProxyField> getListenerInfo(Class clazz, Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
            return null;
        }

        int length = parameters.length;

        String[] variableName;
        try {
            variableName = VariableUtil.getNameByJavassist(clazz, method);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        List<ProxyField> proxyFields = null;
        for (int index = 0; index < length; index++) {
            Parameter parameter = parameters[index];
            if (parameter.isAnnotationPresent(Exclusive.class)) {
                continue;
            }

            ProxyField proxyField = new ProxyField();
            proxyField.setParamIndex(index);
            proxyField.setParamType(parameter.getType());

            Rename rename = parameter.getAnnotation(Rename.class);
            if (null == rename) {
                proxyField.setParamName(variableName[index]);
            } else {
                proxyField.setParamName(rename.value());
            }

            if (null == proxyFields) {
                proxyFields = new ArrayList<>();
            }
            proxyFields.add(proxyField);
        }

        return proxyFields;
    }
}
