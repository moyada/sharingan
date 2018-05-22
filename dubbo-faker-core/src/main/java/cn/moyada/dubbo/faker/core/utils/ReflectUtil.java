package cn.moyada.dubbo.faker.core.utils;


import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {

    private static final Map<String, SoftReference<Field>> fieldMap = new HashMap<>();
    private static final Map<String, SoftReference<Class>> classMap = new HashMap<>();

    public static Class getClassType(String className) throws ClassNotFoundException {
        Class<?> aClass = get(classMap, className);
        if(null != aClass) {
            return aClass;
        }

        aClass = Class.forName(className);
        put(classMap, className, aClass);
        return aClass;
    }

    private static Field getField(Class cls, String fieldName) {
        String key = cls.getName() + fieldName;
        Field field = get(fieldMap, key);
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
        put(fieldMap, key, field);
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

    private static <T> T get(Map<String, SoftReference<T>> map, String key) {
        SoftReference<T> reference = map.get(key);
        if(null == reference) {
            return null;
        }
        return reference.get();
    }

    private static <T> void put(Map<String, SoftReference<T>> map, String key, T value) {
        map.put(key, new SoftReference<>(value));
    }
}
