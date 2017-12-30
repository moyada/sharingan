package cn.xueyikang.dubbo.faker.core.utils;

import cn.xueyikang.dubbo.faker.core.common.HandleInfo;

import java.util.ArrayList;
import java.util.List;

public class ReflectUtil {

    private static final String COMPONENT_TAG = "[]";

    public static String getClassName(Class<?> clazz) {
        return clazz.getName();
    }


    public static Class getClassType(String className) throws ClassNotFoundException {
//        int length = className.length();
//        if(length > 2) {
//            if(COMPONENT_TAG.equals(className.substring(length-2))) {
//
//            }
//        }
        return Class.forName(className);
    }


    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        List<String> l = new ArrayList<String>();
        Class<?>[] parameterTypes = new Class[7];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        parameterTypes[2] = Object.class;
        parameterTypes[3] = double[].class;
        parameterTypes[4] = String[].class;
        parameterTypes[5] = l.getClass();
        parameterTypes[6] = HandleInfo.class;

        System.out.println(getClassName(l.getClass()));
        System.out.println(getClassName(Void.class));
//        System.out.println(getClassName(parameterTypes[0]));
//        System.out.println(getClassName(parameterTypes[1]));
//        System.out.println(getClassName(parameterTypes[2]));
        System.out.println(getClassName(parameterTypes[3]));
        System.out.println(getClassName(parameterTypes[4]));
        System.out.println(getClassName(parameterTypes[5]));
        System.out.println(getClassName(parameterTypes[6]));
        Class classType1 = getClassType("java.lang.Void");
        Class classType = getClassType("[D");
        Class classType2 = getClassType("[I");
        System.out.println(classType);
    }
}
