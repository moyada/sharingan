package cn.moyada.sharingan.instrument.boost.proxy;

import cn.moyada.sharingan.instrument.boost.NameUtil;
import cn.moyada.sharingan.instrument.boost.common.FieldInfo;
import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class JavassistProxy<T> implements ClassProxy {

    static final String PREFIX_NAME = "sharingan";
    static final String LOCAL_VARIABLE = "_invoke_";

    final ClassPool classPool;

    final String invokeInterfaceName, invokeClassName, invokeObjName;

    final String proxyMethod;

    final String invokeParamName;

    final FieldInfo[] privateVariables;

    final StringBuilder invokeBody;

    JavassistProxy(Class invokeClass, Method invokeMethod,
                   Class<T> invokeInterface, Class<? extends T> invokeParam,
                   String[] privateVariables) {

        this.classPool = ClassPool.getDefault();

        this.invokeClassName = invokeClass.getName();
        this.invokeObjName = NameUtil.genPrivateName(invokeClass.getSimpleName());
        this.proxyMethod = invokeMethod.getName();

        this.invokeInterfaceName = invokeInterface.getName();
        this.invokeParamName = invokeParam.getName();

        this.privateVariables = new FieldInfo[privateVariables.length];
        int index = 0;
        FieldInfo fieldInfo;
        for (String privateVariable : privateVariables) {
            fieldInfo = new FieldInfo();
            fieldInfo.setParamName(privateVariable);
            fieldInfo.setPrimitiveName(NameUtil.genPrivateName(privateVariable));
            fieldInfo.setSetMethodName(NameUtil.getSetFunction(privateVariable));

            this.privateVariables[index++] = fieldInfo;
        }

        this.invokeBody = new StringBuilder(128);
    }

    @Override
    public abstract <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws NotFoundException, CannotCompileException;

    protected void addField(CtClass ctClass) throws CannotCompileException {
        // 监控接口
        CtField proxyInvoke = CtField.make("private " + invokeClassName + " " + invokeObjName + ";", ctClass);
        proxyInvoke.setName(invokeObjName);
        addResourceAnnotation(ctClass, proxyInvoke);
        ctClass.addField(proxyInvoke);

        // 属性
        CtField varField;
        for (FieldInfo fieldInfo : privateVariables) {
            varField = getStringField(ctClass, fieldInfo.getPrimitiveName());
            addValueAnnotation(ctClass, varField, "${" + PREFIX_NAME + "." + fieldInfo.getParamName() + "}");
            ctClass.addField(varField);
        }
    }

    protected CtMethod findMethod(CtClass targetClass, ProxyMethod method) throws NotFoundException {
        String methodName = method.getMethodName();
        CtClass[] paramClass = getParamClass(method.getParamTypes());
        try {
            if (null == paramClass) {
                return targetClass.getDeclaredMethod(methodName);
            } else {
                return targetClass.getDeclaredMethod(methodName, paramClass);
            }
        } catch (NotFoundException e) {
            // pass jdk8 default method
            return null;
        }
    }

    public static void addResourceAnnotation(CtClass clazz, CtField cfield) {
        ClassFile cfile = clazz.getClassFile();
        ConstPool cpool = cfile.getConstPool();

        AnnotationsAttribute attr = new AnnotationsAttribute(cpool, AnnotationsAttribute.visibleTag);
        Annotation annot = new Annotation(Resource.class.getName(), cpool);
        attr.addAnnotation(annot);
        cfield.getFieldInfo().addAttribute(attr);
    }

    public static void addValueAnnotation(CtClass clazz, CtField cfield, String value) {
        ClassFile cfile = clazz.getClassFile();
        ConstPool cpool = cfile.getConstPool();

        AnnotationsAttribute attr = new AnnotationsAttribute(cpool, AnnotationsAttribute.visibleTag);
        Annotation annot = new Annotation(Value.class.getName(), cpool);
        annot.addMemberValue("value", new StringMemberValue(value, cpool));
        attr.addAnnotation(annot);
        cfield.getFieldInfo().addAttribute(attr);
    }

    protected void writeFile(CtClass ctClass) {
        String path = getClass().getResource("").getPath();
        String pkg = getClass().getPackage().getName().replace(".", "/");
        int index = path.indexOf(pkg);
        if (-1 == index) {
            return;
        }
        String directoryName = path.substring(0, index);
        try {
            ctClass.writeFile(directoryName);
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    protected CtClass[] getParamClass(Class<?>[] paramTypes) throws NotFoundException {
        if (null == paramTypes) {
            return null;
        }

        int length = paramTypes.length;
        if (length == 0) {
            return null;
        }

        CtClass[] paramClasses = new CtClass[length];
        for (int i = 0; i < length; i++) {
            paramClasses[i] = classPool.get(paramTypes[i].getName());
        }
        return paramClasses;
    }

    protected CtField getStringField(CtClass ctClass, String name) throws CannotCompileException {
        return CtField.make("private String " + name + ";", ctClass);
    }
}
