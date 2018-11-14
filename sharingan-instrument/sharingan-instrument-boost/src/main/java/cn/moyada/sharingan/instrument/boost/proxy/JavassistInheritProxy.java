package cn.moyada.sharingan.instrument.boost.proxy;

import cn.moyada.sharingan.instrument.boost.NameUtil;
import cn.moyada.sharingan.instrument.boost.common.FieldInfo;
import cn.moyada.sharingan.instrument.boost.common.ProxyField;
import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import javassist.*;
import javassist.bytecode.AttributeInfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * javassist 继承代理
 * @author xueyikang
 * @since 0.0.1
 **/
public class JavassistInheritProxy<T> extends JavassistProxy<T> {

    public JavassistInheritProxy(Class invokeClass, Method invokeMethod, Class<T> invokeInterface, Class<? extends T> invokeParam, Map<String, Object> attachParam, String... privateVariables) {
        super(invokeClass, invokeMethod, invokeInterface, invokeParam, attachParam, privateVariables);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws NotFoundException, CannotCompileException {
        CtClass targetClass = classPool.getCtClass(target.getName());

        // 继承方式
        CtClass ctClass = classPool.makeClass(NameUtil.getProxyName(target.getName()));
        ctClass.setSuperclass(targetClass);

        // 注解
        for (AttributeInfo attribute : targetClass.getClassFile().getAttributes()) {
            ctClass.getClassFile().addAttribute(attribute);
        }

        // 构造方法
        CtConstructor ctConstructor;
        for (CtConstructor constructor : targetClass.getConstructors()) {
            ctConstructor = CtNewConstructor.copy(constructor, ctClass, null);
            for (AttributeInfo attribute : constructor.getMethodInfo().getAttributes()) {
                ctConstructor.getMethodInfo().addAttribute(attribute);
            }
            ctConstructor.setBody("super($$);");

            ctClass.addConstructor(ctConstructor);
        }

        addMonitorField(ctClass);

        CtMethod ctMethod;
        for (ProxyMethod method : methods) {
            ctMethod = findMethod(targetClass, method);
            if (null == ctMethod) {
                continue;
            }

            try {
                ctClass.addMethod(superMethod(ctClass, ctMethod, method, invokeObjName, methodName));
            } catch (CannotCompileException e) {
                e.printStackTrace();
                return target;
            }
        }

//        writeFile(ctClass);

        return (Class<T>) ctClass.toClass();
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
    private CtMethod superMethod(CtClass target, CtMethod ctMethod, ProxyMethod method, String objName, String methodName)
            throws CannotCompileException, NotFoundException {

        int modifiers = ctMethod.getModifiers();
        CtClass[] parameterTypes = ctMethod.getParameterTypes();
        CtClass returnType = ctMethod.getReturnType();
        CtClass[] exceptionTypes = ctMethod.getExceptionTypes();

        invokeBody.append("{ \n");

        // condition
        invokeBody.append("if (null != ").append(invokeObjName).append(") {\n");

        // local variable
        invokeBody.append(paramInterfaceName)
                .append(" ")
                .append(LOCAL_VARIABLE)
                .append(" = new ")
                .append(paramClassName)
                .append("();\n");

        // method param
        invokeBody.append(LOCAL_VARIABLE)
                .append(".")
                .append(NameUtil.getSetFunction("protocol"))
                .append("(\"").append(method.getProtocol()).append("\");\n");

        invokeBody.append(LOCAL_VARIABLE)
                .append(".")
                .append(NameUtil.getSetFunction("domain"))
                .append("(\"").append(method.getDomain()).append("\");\n");

        invokeBody.append(LOCAL_VARIABLE)
                .append(".")
                .append(NameUtil.getSetFunction("serializationType"))
                .append("(").append(method.getSerializationType()).append(");\n");


        // application param
        for (FieldInfo fieldInfo : privateVariables) {
            invokeBody.append(LOCAL_VARIABLE)
                    .append(".")
                    .append(fieldInfo.getSetMethodName())
                    .append("(this.")
                    .append(fieldInfo.getPrimitiveName())
                    .append(");\n");
        }

        // attach param
        if (null != attachParam) {
            for (Map.Entry<String, Object> entry : attachParam.entrySet()) {
                invokeBody.append(LOCAL_VARIABLE)
                        .append(".addAttach(\"").append(entry.getKey())
                        .append("\", \"").append(entry.getValue()).append("\");\n");
            }
        }

        // invocation args
        for (ProxyField proxyField : method.getProxyParams()) {
            invokeBody.append(LOCAL_VARIABLE)
                    .append(".addArgs(\"").append(proxyField.getParamName())
                    .append("\", $").append(proxyField.getParamIndex() + 1).append(");\n");
        }

        // invoke method
        invokeBody.append(objName)
                .append(".")
                .append(methodName)
                .append("(")
                .append(LOCAL_VARIABLE)
                .append("); \n }\n");

        if (returnType.getName().equals("void")) {
            invokeBody.append("super.").append(method.getMethodName()).append("($$);\n");
        } else {
            invokeBody.append("return super.").append(method.getMethodName()).append("($$);\n");
        }

        String proxyBody = invokeBody.append("}").toString();

        // clear buffer
        invokeBody.delete(0, invokeBody.length());

        // method annotation
        CtMethod newMethod = CtNewMethod.make(modifiers, returnType, method.getMethodName(), parameterTypes, exceptionTypes, proxyBody, target);
        for (AttributeInfo attribute : ctMethod.getMethodInfo().getAttributes()) {
            newMethod.getMethodInfo().addAttribute(attribute);
        }
        newMethod.setBody(proxyBody);
        return newMethod;
    }
}
