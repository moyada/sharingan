package cn.moyada.dubbo.faker.core.utils;

import java.lang.reflect.Field;

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

    public static Object getValue(Object obj, String field) {
        Class cls = obj.getClass();
        Field f = getField(cls, field);
        if(null == f) {
            return null;
        }
        f.setAccessible(true);

        try {
            return f.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Field getField(Class cls, String fieldName) {
        Field field;
        do {
            if("java.lang.Object".equals(cls.getName())) {
                return null;
            }
            try {
                field = cls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                field = null;
                cls = cls.getSuperclass();
            }
        }
        while (null == field);
        return field;
    }
}
