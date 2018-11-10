package cn.moyada.sharingan.instrument.boost;

import javassist.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final Map<String, String> privateVariables;

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

        this.privateVariables = new HashMap<>(privateVariables.length);
        for (String privateVariable : privateVariables) {
            this.privateVariables.put(NameUtil.genPrivateName(privateVariable), NameUtil.getSetFunction(privateVariable));
        }

        this.invokeBody = new StringBuilder(128);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws NotFoundException, CannotCompileException {
        CtClass targetClass = classPool.getCtClass(target.getName());

        // 继承方式
//        CtClass ctClass = classPool.makeClass(NameUtil.getProxyName(target.getName()));
//        ctClass.setSuperclass(targetClass);

        CtClass ctClass = classPool.getCtClass(target.getName());
        ctClass.setName(NameUtil.getProxyName(ctClass.getName()));

        CtField proxyInvoke = CtField.make("private " + invokeClassName + " " + invokeObjName + " = null;", ctClass);
        proxyInvoke.setName(invokeObjName);
        ctClass.addField(proxyInvoke);

        CtField varField;
        for (String variable : privateVariables.keySet()) {
            varField = getStringField(ctClass, variable);
            ctClass.addField(varField);
        }

        String methodName;
        CtMethod ctMethod;
        CtClass[] paramClass;

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
                insertMethod(ctMethod, method, invokeObjName, proxyMethod);
//                ctClass.addMethod(superMethod(ctMethod, method, invokeObjName, insertMethod));
            } catch (CannotCompileException e) {
                e.printStackTrace();
                return target;
            }

        }

//        try {
//            String path = getClass().getResource("").getPath();
//            String pkg = getClass().getPackage().getName().replace(".", "/");
//            ctClass.writeFile(path.substring(0, path.indexOf(pkg)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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

    /**
     * 继承方法
     * @param ctMethod
     * @param method
     * @param objName
     * @param methodName
     * @return
     * @throws CannotCompileException
     */
    private CtMethod superMethod(CtMethod ctMethod, ProxyMethod method, String objName, String methodName)
            throws CannotCompileException {

        invokeBody.append("{ \n");
        invokeBody.append("if (null != ").append(invokeObjName).append(") {\n");

        invokeBody.append(invokeInterfaceName)
                .append(" ")
                .append(LOCAL_VARIABLE)
                .append(" = new ")
                .append(invokeParamName)
                .append("();\n");

        invokeBody.append(LOCAL_VARIABLE).append(".").append(NameUtil.getSetFunction("protocol")).append("(\"").append(method.getProtocol()).append("\");\n");
        invokeBody.append(LOCAL_VARIABLE).append(".").append(NameUtil.getSetFunction("domain")).append("(\"").append(method.getDomain()).append("\");\n");

        for (Map.Entry<String, String> entry : privateVariables.entrySet()) {
            invokeBody.append(LOCAL_VARIABLE)
                    .append(".")
                    .append(entry.getValue())
                    .append("(this.")
                    .append(entry.getKey())
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
                .append("); \n }\n");

        invokeBody.append("super.").append(method.getMethodName()).append("($$);\n}");

        String proxyBody = invokeBody.toString();

        invokeBody.delete(0, invokeBody.length());

        System.out.println(proxyBody);

        ctMethod.setBody(proxyBody);
        return ctMethod;
    }

    /**
     * 插入方法
     * @param ctMethod
     * @param method
     * @param objName
     * @param methodName
     * @throws CannotCompileException
     */
    private void insertMethod(CtMethod ctMethod, ProxyMethod method, String objName, String methodName)
            throws CannotCompileException {

        invokeBody.append("{ \n");
        invokeBody.append("if (null != ").append(invokeObjName).append(") {\n");

        invokeBody.append(invokeInterfaceName)
                .append(" ")
                .append(LOCAL_VARIABLE)
                .append(" = new ")
                .append(invokeParamName)
                .append("();\n");

        // method param
        invokeBody.append(LOCAL_VARIABLE).append(".").append(NameUtil.getSetFunction("protocol")).append("(\"").append(method.getProtocol()).append("\");\n");
        invokeBody.append(LOCAL_VARIABLE).append(".").append(NameUtil.getSetFunction("domain")).append("(\"").append(method.getDomain()).append("\");\n");

        // application param
        for (Map.Entry<String, String> entry : privateVariables.entrySet()) {
            invokeBody.append(LOCAL_VARIABLE)
                    .append(".")
                    .append(entry.getValue())
                    .append("(this.")
                    .append(entry.getKey())
                    .append(");\n");
        }

        // invocation args
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
