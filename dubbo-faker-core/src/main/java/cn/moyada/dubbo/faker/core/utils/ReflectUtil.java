package cn.moyada.dubbo.faker.core.utils;


import org.jboss.netty.handler.codec.serialization.SoftReferenceMap;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ReflectUtil {

    private static final SoftReferenceMap<String, Field> fieldMap = new SoftReferenceMap<>(new HashMap<>());
    private static final SoftReferenceMap<String, Class> classMap = new SoftReferenceMap<>(new HashMap<>());

    public static Class getClassType(String className) throws ClassNotFoundException {
        Class<?> aClass = classMap.get(className);
        if(null != aClass) {
            return aClass;
        }

        aClass = Class.forName(className);
        classMap.put(className, aClass);
        return aClass;
    }

    private static Field getField(Class cls, String fieldName) {
        String key = cls.getName() + fieldName;
        Field field = fieldMap.get(key);
        if(null != field) {
            return field;
        }

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
        field.setAccessible(true);
        fieldMap.put(key, field);
        return field;
    }

    public static Object getValue(Object obj, String field) {
        Class cls = obj.getClass();
        Field f = getField(cls, field);
        if(null == f) {
            return null;
        }

        try {
            return f.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
