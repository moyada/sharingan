package io.moyada.sharingan.spring.boot.autoconfigure.support;

import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Listener;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Exclusive;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Monitor;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Rename;
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

        ListenerInfo listenerInfo = new ListenerInfo();
        listenerInfo.setListenerMethods(listenerMethod);
        return listenerInfo;
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

    private static List<ListenerMethod> getInvokeMethod(Class<?> clazz) {
        return Collections.emptyList();
    }

    /**
     * 获取方法代理信息，排除静态、私有、不可变方法
     * @param clazz
     */
    private static List<ListenerMethod> getListenerMethod(Class<?> clazz) {
        // Monitor monitor = clazz.getAnnotation(Monitor.class);

        Method[] methods = clazz.getDeclaredMethods();

        List<ListenerMethod> listenerMethods = new ArrayList<>(methods.length);

        for (Method method : methods) {
            if (!needListener(method)) {
                continue;
            }

            Listener value = method.getAnnotation(Listener.class);
            if (null == value) {
                continue;
            }

            List<ProxyField> proxyFields = getListenerInfo(clazz, method);
            if (null == proxyFields) {
                continue;
            }

            ListenerMethod listenerMethod = new ListenerMethod();
            listenerMethod.setDomain(value.value());
            listenerMethod.setMethodName(method.getName());
            listenerMethod.setParamTypes(method.getParameterTypes());
            listenerMethod.setSerializationType(value.serialization().getDeclaringClass().getName()
                    + "." + value.serialization().name());

            listenerMethod.setProxyParams(proxyFields);

            listenerMethods.add(listenerMethod);
        }

        return listenerMethods;
    }
    /**
     * 获取方法代理信息，排除静态、私有、不可变方法
     * @param clazz
     */
    private static void appendProxyMethod(Map<String, ListenerMethod> proxyMethods, Class<?> clazz) {
        Monitor monitor = clazz.getAnnotation(Monitor.class);

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (!needListener(method)) {
                continue;
            }

            Listener annotation = method.getAnnotation(Listener.class);
            if (null == annotation) {
                continue;
            }

            String name = getName(method);
            if (proxyMethods.containsKey(name)) {
                continue;
            }

            List<ProxyField> proxyFields = getListenerInfo(clazz, method);
            if (null == proxyFields) {
                continue;
            }

            ListenerMethod listenerMethod = new ListenerMethod();
            listenerMethod.setDomain(annotation.value());
            listenerMethod.setMethodName(method.getName());
            listenerMethod.setParamTypes(method.getParameterTypes());

            listenerMethod.setSerializationType(annotation.serialization().getDeclaringClass().getName()
                    + "." + annotation.serialization().name());

            listenerMethod.setProxyParams(proxyFields);

            proxyMethods.put(name, listenerMethod);
        }
    }

    private static boolean needListener(Method method) {
        if (method.getParameterCount() == 0) {
            return false;
        }
        return (method.getModifiers() & (Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)) == 0;
    }

    private static String getName(Method method) {
        StringBuilder methodName = new StringBuilder(method.getName());
        methodName.append('[');
        for (Class<?> parameterType : method.getParameterTypes()) {
            methodName.append(parameterType.getName()).append(',');
        }
        return methodName.deleteCharAt(methodName.length()-1).append(']').toString();
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
