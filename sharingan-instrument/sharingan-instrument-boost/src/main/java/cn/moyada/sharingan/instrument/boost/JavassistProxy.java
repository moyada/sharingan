package cn.moyada.sharingan.instrument.boost;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class JavassistProxy<T> implements ClassProxy {

    private final String LOCAL_VARIABLE = "_invoke";

    private final ClassPool classPool;

    private final String invokeClassName;

    private final String invokeObjName;

    private final String invokeInterfaceName;
    private final String invokeParamName;

    private final String proxyMethod;

    private final String[] privateVariables;

    private final StringBuilder invokeBody;

    public JavassistProxy(Class invokeClass, Method invokeMethod,
                          Class<T> invokeInterface, Class<? extends T> invokeParam,
                          String[] privateVariables) {

        this.classPool = ClassPool.getDefault();

        this.invokeClassName = invokeClass.getName();
        this.invokeObjName = NameUtil.genPrivateName(invokeClass.getSimpleName());
        this.proxyMethod = invokeMethod.getName();

        this.invokeInterfaceName = invokeInterface.getName();
        this.invokeParamName = invokeParam.getName();

        this.privateVariables = new String[privateVariables.length];
        int index = 0;
        for (String privateVariable : privateVariables) {
            this.privateVariables[index++] = NameUtil.genPrivateName(privateVariable);
        }

        this.invokeBody = new StringBuilder(128);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws NotFoundException, CannotCompileException {
        CtClass ctClass = classPool.getCtClass(target.getName());

        CtField proxyInvoke = CtField.make("private " + invokeClassName + " " + invokeObjName + " = null;", ctClass);
        proxyInvoke.setName(invokeObjName);

        CtField varField;
        for (String variable : privateVariables) {
            varField = getStringField(ctClass, variable);
            ctClass.addField(varField);
        }

        ctClass.addField(proxyInvoke);

        String methodName;
        CtMethod ctMethod;
        CtClass[] paramClass;

        CtClass targetClass = classPool.getCtClass(target.getName());

        for (ProxyMethod method : methods) {
            methodName = method.getMethodName();
            paramClass = getParamClass(method.getProxyParams());

            try {
                if (null == paramClass) {
                    ctMethod = targetClass.getDeclaredMethod(methodName);
                } else {
                    ctMethod = targetClass.getDeclaredMethod(methodName, paramClass);
                }
            } catch (NotFoundException e) {
                // pass jdk8 default method
                continue;
            }

            try {
                proxyMethod(ctMethod, method, invokeObjName, proxyMethod);
            } catch (CannotCompileException e) {
                e.printStackTrace();
                return target;
            }
        }

        ctClass.setName(NameUtil.getProxyName(ctClass.getName()));

        try {
            String path = getClass().getResource("").getPath();
            String pkg = getClass().getPackage().getName().replace(".", "/");
            ctClass.writeFile(path.substring(0, path.indexOf(pkg)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (Class<T>) ctClass.toClass();
    }

    private CtClass[] getParamClass(List<ProxyField> proxyFields) throws NotFoundException {
        if (null == proxyFields || proxyFields.size() == 0) {
            return null;
        }

        CtClass[] paramClasses = new CtClass[proxyFields.size()];
        for (int i = 0; i < proxyFields.size(); i++) {
            paramClasses[i] = classPool.get(proxyFields.get(i).getParamType().getName());
        }
        return paramClasses;
    }

    private CtField getStringField(CtClass ctClass, String name) throws CannotCompileException {
        return CtField.make("private String " + name + ";", ctClass);
    }

    private void proxyMethod(CtMethod ctMethod, ProxyMethod method, String objName, String methodName)
            throws CannotCompileException {
//        ctMethod.addLocalVariable(LOCAL_VARIABLE, paramFile);

        invokeBody.append("{ \n");
        invokeBody.append("if (null != ").append(invokeObjName).append(") {\n");

        invokeBody.append(invokeInterfaceName)
                .append(" ")
                .append(LOCAL_VARIABLE)
                .append(" = new ")
                .append(invokeParamName)
                .append("();\n");

        invokeBody.append(LOCAL_VARIABLE).append(".addArgs(\"_protocol\", \"").append(method.getProtocol()).append("\");\n");
        invokeBody.append(LOCAL_VARIABLE).append(".addArgs(\"_domain\", \"").append(method.getDomain()).append("\");\n");

        for (String variable : privateVariables) {
            invokeBody.append(LOCAL_VARIABLE)
                    .append(".addArgs(\"")
                    .append(variable)
                    .append("\", this.")
                    .append(variable)
                    .append(");\n");
        }

        for (ProxyField proxyField : method.getProxyParams()) {
            invokeBody.append(LOCAL_VARIABLE)
                    .append(".addArgs(\"").append(proxyField.getParamName())
                    .append("\", $").append(proxyField.getParamIndex() + 1).append(");\n");
        }

        invokeBody.append(objName)
                .append(".")
                .append(methodName)
                .append("(")
                .append(LOCAL_VARIABLE)
                .append("); \n }\n }");

        String proxyBody = invokeBody.toString();

        invokeBody.delete(0, invokeBody.length());

        if (method.isProxyBefore()) {
            ctMethod.insertBefore(proxyBody);
        }
        if (method.isProxyAfter()) {
            ctMethod.insertAfter(proxyBody);
        }
    }
}
