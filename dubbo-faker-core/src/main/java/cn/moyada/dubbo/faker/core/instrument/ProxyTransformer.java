package cn.moyada.dubbo.faker.core.instrument;

import com.alibaba.dubbo.common.bytecode.Proxy;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author xueyikang
 * @create 2018-04-29 22:58
 */
public class ProxyTransformer implements ClassFileTransformer {


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if("com.alibaba.dubbo.common.bytecode.Proxy".equals(className)){
            try {
                //通过javassist修改sayHello方法字节码

                ClassPool classPool = ClassPool.getDefault();
                ClassClassPath classPath = new ClassClassPath(Proxy.class);
                classPool.insertClassPath(classPath);

                CtClass ctClass = classPool.getCtClass(className);
                CtClass paramType = classPool.getCtClass("java.lang.Class[]");
                CtMethod ctMethod = ctClass.getDeclaredMethod("getProxy", new CtClass[]{paramType});

                // 替换为获取当前线程类加载器
                ctMethod.instrument(
                        new ExprEditor() {
                            public void edit(MethodCall m) throws CannotCompileException {
                                m.replace("$_ = getProxy(Thread.currentThread().getContextClassLoader(), $sig);");
                            }
                        });
                return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return classfileBuffer;
    }
}
