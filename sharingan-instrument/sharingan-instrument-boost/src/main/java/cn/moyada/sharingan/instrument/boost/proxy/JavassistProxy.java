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
import java.util.Map;

/**
 * javassist 代理
 * @author xueyikang
 * @since 0.0.1
 **/
public abstract class JavassistProxy<T> implements ClassProxy {

    private static final String PREFIX_NAME = "sharingan";

    // 临时字段名
    static final String LOCAL_VARIABLE = NameUtil.genPrivateName("invoke");

    final ClassPool classPool;

    // 调用器类名、对象名
    final String invokeClassName, invokeObjName;

    // 调用方法名
    final String methodName;

    // 传输属性接口类名、实现类名
    final String paramInterfaceName, paramClassName;

    // 服务包含属性
    final FieldInfo[] privateVariables;

    // 主机额外属性
    final Map<String, Object> attachParam;

    final StringBuilder invokeBody;

    JavassistProxy(Class invokeClass, Method invokeMethod,
                   Class<T> invokeInterface, Class<? extends T> invokeParam,
                   Map<String, Object> attachParam, String... privateVariables) {

        this.classPool = ClassPool.getDefault();

        this.invokeClassName = invokeClass.getName();
        this.invokeObjName = NameUtil.genPrivateName(invokeClass.getSimpleName());
        this.methodName = invokeMethod.getName();

        this.paramInterfaceName = invokeInterface.getName();
        this.paramClassName = invokeParam.getName();

        this.attachParam = attachParam;

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

    /**
     * 增加调用监视器字段
     * @param ctClass
     * @throws CannotCompileException
     */
    void addMonitorField(CtClass ctClass) throws CannotCompileException {
        // 监控接口
        CtField proxyInvoke = CtField.make("private " + invokeClassName + " " + invokeObjName + ";", ctClass);
        proxyInvoke.setName(invokeObjName);
        addResourceAnnotation(ctClass, proxyInvoke);
        ctClass.addField(proxyInvoke);

        // 增加系统属性
        CtField varField;
        for (FieldInfo fieldInfo : privateVariables) {
            varField = getStringField(ctClass, fieldInfo.getPrimitiveName());
            addValueAnnotation(ctClass, varField, "${" + PREFIX_NAME + "." + fieldInfo.getParamName() + "}");
            ctClass.addField(varField);
        }
    }

    /**
     * 查找方法信息
     * @param targetClass
     * @param method
     * @return
     * @throws NotFoundException
     */
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

    /**
     * 增加 {@code Resource} 注解
     * @param clazz
     * @param cfield
     */
    private void addResourceAnnotation(CtClass clazz, CtField cfield) {
        ClassFile cfile = clazz.getClassFile();
        ConstPool cpool = cfile.getConstPool();

        AnnotationsAttribute attr = new AnnotationsAttribute(cpool, AnnotationsAttribute.visibleTag);
        Annotation annot = new Annotation(Resource.class.getName(), cpool);
        attr.addAnnotation(annot);
        cfield.getFieldInfo().addAttribute(attr);
    }

    /**
     * 增加 {@code Value} 注解，设置属性注入表达式
     * @param clazz
     * @param cfield
     * @param value
     */
    private void addValueAnnotation(CtClass clazz, CtField cfield, String value) {
        ClassFile cfile = clazz.getClassFile();
        ConstPool cpool = cfile.getConstPool();

        AnnotationsAttribute attr = new AnnotationsAttribute(cpool, AnnotationsAttribute.visibleTag);
        Annotation annot = new Annotation(Value.class.getName(), cpool);
        annot.addMemberValue("value", new StringMemberValue(value, cpool));
        attr.addAnnotation(annot);
        cfield.getFieldInfo().addAttribute(attr);
    }

    /**
     * 将生成的 class 类写入输入路径
     * @param ctClass
     */
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

    /**
     * 解析类信息
     * @param paramTypes
     * @return
     * @throws NotFoundException
     */
    private CtClass[] getParamClass(Class<?>[] paramTypes) throws NotFoundException {
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

    /**
     * 生成 {@code String} 属性
     * @param ctClass
     * @param name
     * @return
     * @throws CannotCompileException
     */
    private CtField getStringField(CtClass ctClass, String name) throws CannotCompileException {
        return CtField.make("private String " + name + ";", ctClass);
    }
}
