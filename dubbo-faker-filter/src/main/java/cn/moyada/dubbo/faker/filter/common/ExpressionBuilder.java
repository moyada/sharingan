package cn.moyada.dubbo.faker.filter.common;

import cn.moyada.dubbo.faker.filter.log.LoggerBuilder;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author xueyikang
 * @create 2018-05-06 10:18
 */
public class ExpressionBuilder {

    public static void get(Class<?> clazz, Method method) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(clazz.getName());
            System.out.println(cc.toString());
            System.out.println(new String(cc.toBytecode(),"UTF-8"));
//            CtMethod cm = cc.getDeclaredMethod(method.getName());
//            System.out.println(cm.getLongName());
//            System.out.println(cm.getMethodInfo2().getAttributes().toString());
//            System.out.println(cm.getMethodInfo2().getCodeAttribute().getAttribute(LocalVariableAttribute.tag));
//            MethodInfo methodInfo = cm.getMethodInfo();
//            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
//            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
//            if (attr != null) {
//                String[] paramNames = new String[cm.getParameterTypes().length];
//                int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
//                for (int i = 0; i < paramNames.length; i++)
//                    paramNames[i] = attr.variableName(i + pos);
//            }

        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
//        for (Parameter parameter : method.getParameters()) {
//            System.out.println(parameter.getName());
//        }
    }

    public static void main(String[] args) {
        Method[] declaredMethods = LoggerBuilder.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            get(LoggerBuilder.class, method);
        }

    }
}
