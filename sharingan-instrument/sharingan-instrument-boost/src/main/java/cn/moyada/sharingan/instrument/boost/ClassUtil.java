package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.Catch;
import cn.moyada.sharingan.monitor.api.Listener;
import cn.moyada.sharingan.monitor.api.Rename;
import javassist.NotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ClassUtil {


    public static List<ProxyMethod> getProxyInfo(Class clazz,
                                          Class<? extends Annotation> exclusiveAnnotation) {
        if (null == clazz) {
            return null;
        }
        if (clazz.isInterface()) {
            return null;
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return null;
        }

        Map<Class, Listener> annotationClass = getAnnotationClass(clazz);
        if (annotationClass.isEmpty()) {
            return null;
        }

        List<ProxyMethod> proxyMethods = getMethodInfo(annotationClass, exclusiveAnnotation);
        if (null == proxyMethods) {
            return null;
        }

        try {
            checkMethodVariable(clazz, proxyMethods);
        } catch (NotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        return proxyMethods;
    }

    private static Map<Class, Listener> getAnnotationClass(Class<?> clazz) {
        Map<Class, Listener> classes = new HashMap<>();

        Listener annotation = clazz.getAnnotation(Listener.class);
        if (null != annotation) {
            classes.put(clazz, annotation);
        }

        addAnnotationInterface(clazz.getInterfaces(), classes);
        addAnnotationSuper(clazz.getSuperclass(), classes);
        return classes;
    }

    private static void addAnnotationInterface(Class<?>[] interfaces, Map<Class, Listener> classes) {
        for (Class<?> anInterface : interfaces) {
            Listener annotation = anInterface.getAnnotation(Listener.class);
            if (null != annotation) {
                classes.put(anInterface, annotation);
            }
            addAnnotationInterface(anInterface.getInterfaces(), classes);
        }
    }

    private static void addAnnotationSuper(Class<?> superClass, Map<Class, Listener> classes) {
        if (null == superClass) {
            return;
        }
        Listener annotation = superClass.getAnnotation(Listener.class);
        if (null != annotation) {
            classes.put(superClass, annotation);
        }
        addAnnotationSuper(superClass.getSuperclass(), classes);
    }

    private static List<ProxyMethod> getMethodInfo(Map<Class, Listener> targetClass,
                                                   Class<? extends Annotation> exclusiveAnnotation) {
        Map<String, ProxyMethod> proxyMethods = new HashMap<>();

        for (Map.Entry<Class, Listener> entry : targetClass.entrySet()) {
            addMethodInfo(entry.getKey(), entry.getValue(), proxyMethods, exclusiveAnnotation);
        }

        if (proxyMethods.isEmpty()) {
            return null;
        }

        List<ProxyMethod> proxyMethod = new ArrayList<>(proxyMethods.size());
        proxyMethod.addAll(proxyMethods.values());
        return proxyMethod;
    }

    private static void addMethodInfo(Class clazz, Listener listener, Map<String, ProxyMethod> proxyMethods,
                                               Class<? extends Annotation> exclusiveAnnotation) {
        List<ProxyField> proxyFields;

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (method.getParameterCount() == 0) {
                continue;
            }
            Catch annotation = method.getAnnotation(Catch.class);
            if (null == annotation) {
                continue;
            }

            String methodName = method.getName();
            if (proxyMethods.containsKey(methodName)) {
                continue;
            }

            proxyFields = getParamInfo(clazz, method, exclusiveAnnotation);
            if (null == proxyFields) {
                continue;
            }

            ProxyMethod proxyMethod = new ProxyMethod();
            proxyMethod.setMethodName(methodName);
            proxyMethod.setProxyParams(proxyFields);
            proxyMethod.setProxyBefore(true);

            String domain = annotation.value();
            if (NameUtil.isEmpty(domain)) {
                domain = listener.domain();
            }
            proxyMethod.setDomain(domain);
            proxyMethod.setProtocol(listener.protocol().getProtocol());

            proxyMethods.put(methodName, proxyMethod);
        }
    }

    private static List<ProxyField> getParamInfo(Class clazz, Method method, Class<? extends Annotation> exclusiveAnnotation) {
        Parameter[] parameters = method.getParameters();
        if (null == parameters || parameters.length == 0) {
            return null;
        }

        int length = parameters.length;

        String[] variableName;
        try {
            variableName = VariableUtil.getNameByJavassist(clazz, method);
        } catch (NotFoundException e) {
            e.printStackTrace();
            variableName = null;
        }

        List<ProxyField> proxyFields = null;
        for (int index = 0; index < length; index++) {
            Parameter parameter = parameters[index];
            if (parameter.isAnnotationPresent(exclusiveAnnotation)) {
                continue;
            }

            ProxyField proxyField = new ProxyField();
            proxyField.setParamIndex(index);
            proxyField.setParamType(parameter.getType());

            Rename rename = parameter.getAnnotation(Rename.class);
            if (null == rename) {
                proxyField.setParamName(null == variableName ? null : variableName[index]);
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

    private static void checkMethodVariable(Class target, List<ProxyMethod> proxyMethods) throws NoSuchMethodException, NotFoundException {
        String[] variableName = null;
        for (ProxyMethod proxyMethod : proxyMethods) {
            List<ProxyField> proxyParams = proxyMethod.getProxyParams();

            for (ProxyField proxyField : proxyParams) {
                if (null == proxyField.getParamName()) {
                    if (null == variableName) {
                        Class[] paramTypes = proxyMethod.getParamTypes();
                        @SuppressWarnings("unchecked")
                        Method method = target.getDeclaredMethod(proxyMethod.getMethodName(), paramTypes);
                        variableName = VariableUtil.getNameByJavassist(target, method);
                    }

                    proxyField.setParamName(variableName[proxyField.getParamIndex()]);
                }
            }
            variableName = null;
        }
    }
}
