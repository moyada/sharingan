package cn.xueyikang.dubbo.faker.core.handle;

import cn.xueyikang.dubbo.faker.core.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodInvokeHandle extends AbstractHandle {
    private static final Logger log = LoggerFactory.getLogger(MethodInvokeHandle.class);

    @Override
    public MethodHandle fetchHandleInfo(String className, String methodName, String returnType, Class<?> paramClass[]) {
        Class<?> classType, returnClassType;

        try {
            classType = ReflectUtil.getClassType(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Take Invoke Class Error: " + e);
        }

        try {
            returnClassType = ReflectUtil.getClassType(returnType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Take Return Class Error: " + e);
        }


        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(returnClassType, paramClass);

        MethodHandle methodHandle;
        try {
            methodHandle = lookup.findVirtual(classType, methodName, methodType);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException("Take Method Class Error: " + e);
        }
        return methodHandle;
    }

    public static void main(String[] args) {
        System.out.println(String.class.getName());
    }
}
