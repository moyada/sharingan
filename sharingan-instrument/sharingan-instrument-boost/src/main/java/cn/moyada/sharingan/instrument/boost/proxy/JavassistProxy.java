package cn.moyada.sharingan.instrument.boost.proxy;

import cn.moyada.sharingan.instrument.boost.NameUtil;
import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import javassist.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class JavassistProxy<T> implements ClassProxy {

    final String LOCAL_VARIABLE = "_invoke_";

    final ClassPool classPool;

    final String invokeInterfaceName, invokeClassName, invokeObjName;

    final String proxyMethod;

    final String invokeParamName;

    final Map<String, String> privateVariables;

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

        this.privateVariables = new HashMap<>(privateVariables.length);
        for (String privateVariable : privateVariables) {
            this.privateVariables.put(NameUtil.genPrivateName(privateVariable), NameUtil.getSetFunction(privateVariable));
        }

        this.invokeBody = new StringBuilder(128);
    }

    @Override
    public abstract <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws NotFoundException, CannotCompileException;

    void writeFile(CtClass ctClass) {
        try {
            String path = getClass().getResource("").getPath();
            String pkg = getClass().getPackage().getName().replace(".", "/");
            ctClass.writeFile(path.substring(0, path.indexOf(pkg)));
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    CtClass[] getParamClass(Class<?>[] paramTypes) throws NotFoundException {
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


    CtField getStringField(CtClass ctClass, String name) throws CannotCompileException {
        return CtField.make("private String " + name + ";", ctClass);
    }
}
