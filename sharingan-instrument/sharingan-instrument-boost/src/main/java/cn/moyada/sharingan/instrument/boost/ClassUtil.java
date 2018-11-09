package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.Rename;

import java.io.IOException;
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
                                          Class<? extends Annotation> classAnnotation,
                                          Class<? extends Annotation> methodAnnotation,
                                          Class<? extends Annotation> exclusiveAnnotation) {
        if (null == clazz) {
            return null;
        }
        if (null == classAnnotation) {
            return null;
        }
        if (null == methodAnnotation) {
            return null;
        }
        if (clazz.isInterface()) {
            return null;
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return null;
        }

        List<Class> targetClass = getAnnotationClass(clazz, classAnnotation);
        if (targetClass.isEmpty()) {
            return null;
        }

        List<ProxyMethod> proxyMethods = getMethodInfo(targetClass, methodAnnotation, exclusiveAnnotation);
        if (null == proxyMethods) {
            return null;
        }

        try {
            checkMethodVariable(clazz, proxyMethods);
        } catch (IOException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        return proxyMethods;
    }

    private static List<Class> getAnnotationClass(Class clazz, Class<? extends Annotation> classAnnotation) {
        List<Class> classes = new ArrayList<>();

        if (clazz.isAnnotationPresent(classAnnotation)) {
            classes.add(clazz);
        }

        addAnnotationInterface(clazz.getInterfaces(), classAnnotation, classes);
        addAnnotationSuper(clazz.getSuperclass(), classAnnotation, classes);
        return classes;
    }

    private static void addAnnotationInterface(Class[] interfaces, Class<? extends Annotation> classAnnotation, List<Class> classes) {
        for (Class anInterface : interfaces) {
            if (anInterface.isAnnotationPresent(classAnnotation)) {
                classes.add(anInterface);
            }
            addAnnotationInterface(anInterface.getInterfaces(), classAnnotation, classes);
        }
    }

    private static void addAnnotationSuper(Class superClass, Class<? extends Annotation> classAnnotation, List<Class> classes) {
        if (null == superClass) {
            return;
        }

        if (superClass.isAnnotationPresent(classAnnotation)) {
            classes.add(superClass);
        }

        addAnnotationSuper(superClass.getSuperclass(), classAnnotation, classes);
    }

    private static List<ProxyMethod> getMethodInfo(List<Class> targetClass,
                                                   Class<? extends Annotation> methodAnnotation,
                                                   Class<? extends Annotation> exclusiveAnnotation) {
        Map<String, ProxyMethod> proxyMethods = new HashMap<>();
        for (Class clazz : targetClass) {
            addMethodInfo(clazz, proxyMethods, methodAnnotation, exclusiveAnnotation);
        }

        if (proxyMethods.isEmpty()) {
            return null;
        }

        List<ProxyMethod> proxyMethod = new ArrayList<>(proxyMethods.size());
        proxyMethod.addAll(proxyMethods.values());
        return proxyMethod;
    }

    private static void addMethodInfo(Class clazz, Map<String, ProxyMethod> proxyMethods,
                                               Class<? extends Annotation> methodAnnotation,
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
            if (!method.isAnnotationPresent(methodAnnotation)) {
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
            variableName = VariableUtil.getNameByASM(clazz, method);
        } catch (IOException e) {
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

    private static void checkMethodVariable(Class target, List<ProxyMethod> proxyMethods) throws NoSuchMethodException, IOException {
        String[] variableName = null;
        for (ProxyMethod proxyMethod : proxyMethods) {
            List<ProxyField> proxyParams = proxyMethod.getProxyParams();

            for (ProxyField proxyField : proxyParams) {
                if (null == proxyField.getParamName()) {
                    if (null == variableName) {
                        Class[] paramTypes = proxyMethod.getParamTypes();
                        @SuppressWarnings("unchecked")
                        Method method = target.getDeclaredMethod(proxyMethod.getMethodName(), paramTypes);
                        variableName = VariableUtil.getNameByASM(target, method);
                    }

                    proxyField.setParamName(variableName[proxyField.getParamIndex()]);
                }
            }
            variableName = null;
        }
    }
}
