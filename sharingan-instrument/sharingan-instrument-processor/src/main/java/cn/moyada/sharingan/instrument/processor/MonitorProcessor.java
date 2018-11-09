package cn.moyada.sharingan.instrument.processor;


import cn.moyada.sharingan.instrument.boost.*;
import cn.moyada.sharingan.monitor.api.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
//@AutoService(Processor.class)
public class MonitorProcessor extends AbstractProcessor {

    private final Class invokeClass;
    private final Method invokeMethod;
    private final Class invokeParam;
    private final Class invokeParamReal;
    private final String[] privateVariables;

    private MessagePrint messagePrint;
    private Filer filer;

    public MonitorProcessor() throws NoSuchMethodException {
        this.invokeClass = Monitor.class;
        this.invokeMethod = Monitor.class.getMethod("listener", Invocation.class);
        this.invokeParam = Invocation.class;
        this.invokeParamReal = DefaultInvocation.class;
        this.privateVariables = new String[]{"application", "domain"};
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        if (null == messagePrint) {
            messagePrint = new MessagePrint(processingEnv.getMessager());
        }
        messagePrint.info(getClass().getSimpleName() + " Init");
        filer = processingEnv.getFiler();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> proxyElements = new HashSet<>();
        TypeMirror typeMirror;
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Listener.class);
        for (Element element : elementsAnnotatedWith) {
            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }

            typeMirror = element.asType();
            if (typeMirror == null) {
                continue;
            }

            if (element instanceof TypeElement) {
                proxyElements.add((TypeElement) element);
            }
        }
        if (proxyElements.isEmpty()) {
            return true;
        }

        List<ProxyMethod> proxyMethods;
        Map<String, List<ProxyMethod>> proxyClass = new HashMap<>();
        for (TypeElement proxyElement : proxyElements) {
            proxyMethods = build(proxyElement);
            if (null == proxyMethods) {
                continue;
            }
            proxyClass.put(proxyElement.asType().toString(), proxyMethods);
        }
        if (proxyClass.isEmpty()) {
            return true;
        }

        ClassProxy proxy;
        try {
            proxy = new JavassistProxy(invokeClass, invokeMethod, invokeParam, invokeParamReal, privateVariables);
        } catch (Exception e) {
            messagePrint.error(e.getMessage());
            return true;
        }

        proxy(proxy, proxyClass);

        return true;
    }

    private void proxy(ClassProxy proxy, Map<String, List<ProxyMethod>> proxyClass) {
        String className;
        Class<?> target;
        List<ProxyMethod> proxyMethods;

        for (Map.Entry<String, List<ProxyMethod>> entry : proxyClass.entrySet()) {
            className = entry.getKey();
            proxyMethods = entry.getValue();

            try {
                target = Class.forName(className);
            } catch (ClassNotFoundException e) {
                messagePrint.error(e.getMessage());
                continue;
            }

            try {
                proxy.wrapper(target, proxyMethods);
            } catch (Exception e) {
                messagePrint.error(e.getMessage());
            }
        }
    }

    private List<ProxyMethod> build(TypeElement element) {
        List<ExecutableElement> methods = getAllMethods(element, Catch.class);
        if (methods.isEmpty()) {
            return null;
        }

        List<ProxyMethod> proxyMethods = new ArrayList<>(methods.size());
        ProxyMethod proxyMethod;
        for (ExecutableElement method : methods) {
            List<ProxyField> parameter = getParameter(method, Exclusive.class);
            if (null == parameter) {
                continue;
            }

            proxyMethod = new ProxyMethod();
            proxyMethod.setMethodName(method.getSimpleName().toString());
            proxyMethod.setProxyParams(parameter);
            proxyMethod.setProxyBefore(true);

            proxyMethods.add(proxyMethod);
        }

        return proxyMethods;
    }

    private List<ProxyField> getParameter(ExecutableElement element, Class<? extends Annotation> exclusive) {
        List<VariableElement> elements = ElementFilter.fieldsIn(element.getEnclosedElements());
        int size = elements.size();
        if (size == 0) {
            return null;
        }

        List<ProxyField> proxyFields = new ArrayList<>();

        ProxyField proxyField;
        VariableElement variableElement;
        for (int i = 0; i < size; i++) {
            variableElement = elements.get(i);
            Object value = variableElement.getAnnotation(exclusive);
            if (value != null) {
                continue;
            }

            proxyField = new ProxyField();

            String name = variableElement.getSimpleName().toString();
            Rename annotation = variableElement.getAnnotation(Rename.class);
            if (null != annotation) {
                name = annotation.value();
            }

            proxyField.setParamIndex(i);
            proxyField.setParamName(name);
            proxyFields.add(proxyField);
        }

        if (proxyFields.isEmpty()) {
            return null;
        }
        return proxyFields;
    }

    private List<ExecutableElement> getAllMethods(TypeElement type, Class<? extends Annotation> annotation) {
        List<ExecutableElement> elements = ElementFilter.methodsIn(type.getEnclosedElements());

        List<ExecutableElement> proxyMethods = new ArrayList<>();
        for (ExecutableElement element : elements) {
            Object value = element.getAnnotation(annotation);
            if (value != null) {
                proxyMethods.add(element);
            }
        }
        return proxyMethods;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new HashSet<>(3);
        annotationTypes.add(Listener.class.getName());
        annotationTypes.add(Catch.class.getName());
        annotationTypes.add(Rename.class.getName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        if (SourceVersion.latestSupported().compareTo(SourceVersion.RELEASE_7) > 0) {
            return SourceVersion.latestSupported();
        }
        return SourceVersion.RELEASE_7;
    }
}
