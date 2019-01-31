package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.instrument.boost.common.ProxyField;
import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import cn.moyada.sharingan.monitor.api.annotation.Catch;
import cn.moyada.sharingan.monitor.api.annotation.Listener;
import cn.moyada.sharingan.monitor.api.annotation.Rename;
import javassist.NotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类代理工具
 * @author xueyikang
 * @since 0.0.1
 **/
public class ClassUtil {

    private static final int METHOD_MODIFIER = Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE;

    /**
     * 获取代理信息
     * @param clazz
     * @param exclusiveAnnotation
     * @return
     */
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

    /**
     * 获取含有注解的类、父类、接口
     * @param clazz
     * @return
     */
    private static Map<Class, Listener> getAnnotationClass(Class<?> clazz) {
        Map<Class, Listener> classes = new HashMap<>();

        Listener annotation = clazz.getAnnotation(Listener.class);
        if (null != annotation && isNotFinal(clazz)) {
            classes.put(clazz, annotation);
        }

        addAnnotationInterface(clazz.getInterfaces(), classes);
        addAnnotationSuper(clazz.getSuperclass(), classes);
        return classes;
    }

    /**
     * 获取注解接口
     * @param interfaces
     * @param classes
     */
    private static void addAnnotationInterface(Class<?>[] interfaces, Map<Class, Listener> classes) {
        for (Class<?> anInterface : interfaces) {
            Listener annotation = anInterface.getAnnotation(Listener.class);
            if (null != annotation) {
                classes.put(anInterface, annotation);
            }
            addAnnotationInterface(anInterface.getInterfaces(), classes);
        }
    }

    /**
     * 获取注解父类
     * @param superClass
     * @param classes
     */
    private static void addAnnotationSuper(Class<?> superClass, Map<Class, Listener> classes) {
        if (null == superClass) {
            return;
        }
        Listener annotation = superClass.getAnnotation(Listener.class);
        if (null != annotation && isNotFinal(superClass)) {
            classes.put(superClass, annotation);
        }
        addAnnotationSuper(superClass.getSuperclass(), classes);
    }

    private static boolean isNotFinal(Class clazz) {
        return !Modifier.isFinal(clazz.getModifiers());
    }

    /**
     * 获取标记方法
     * @param targetClass
     * @param exclusiveAnnotation
     * @return
     */
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

    /**
     * 获取方法代理信息，排除静态、私有、不可变方法
     * @param clazz
     * @param listener
     * @param proxyMethods
     * @param exclusiveAnnotation
     */
    private static void addMethodInfo(Class clazz, Listener listener, Map<String, ProxyMethod> proxyMethods,
                                               Class<? extends Annotation> exclusiveAnnotation) {
        List<ProxyField> proxyFields;

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (isStaticOrFinalOrPrivate(method)) {
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
            proxyMethod.setParamTypes(method.getParameterTypes());
            proxyMethod.setSerializationType(annotation.serialization().name());
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

    private static boolean isStaticOrFinalOrPrivate(Method method) {
        return (METHOD_MODIFIER & method.getModifiers()) != 0;
    }

    /**
     * 获取方法参数代理信息
     * @param clazz
     * @param method
     * @param exclusiveAnnotation
     * @return
     */
    private static List<ProxyField> getParamInfo(Class clazz, Method method, Class<? extends Annotation> exclusiveAnnotation) {
        Class[] parameters = method.getParameterTypes();
        if (parameters.length == 0) {
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
            Class<?> parameter = parameters[index];
            if (parameter.isAnnotationPresent(exclusiveAnnotation)) {
                continue;
            }

            ProxyField proxyField = new ProxyField();
            proxyField.setParamIndex(index);
            proxyField.setParamType(parameter);

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

    /**
     * 重新获取属性名
     * @param target
     * @param proxyMethods
     * @throws NoSuchMethodException
     * @throws NotFoundException
     */
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

                        if (null == variableName) {
                            proxyMethods.remove(proxyMethod);
                            break;
                        }
                    }

                    proxyField.setParamName(variableName[proxyField.getParamIndex()]);
                }
            }
            variableName = null;
        }
    }
}
