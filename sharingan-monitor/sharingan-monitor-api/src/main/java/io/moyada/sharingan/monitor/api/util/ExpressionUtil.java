package io.moyada.sharingan.monitor.api.util;

import io.moyada.sharingan.monitor.api.entity.FunctionInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ExpressionUtil {

    private static Map<Class<?>, Class<?>> primitiveMap = new HashMap<>();

    static {
        primitiveMap.put(byte.class, Byte.class);
        primitiveMap.put(short.class, Short.class);
        primitiveMap.put(int.class, Integer.class);
        primitiveMap.put(long.class, Long.class);
        primitiveMap.put(float.class, Float.class);
        primitiveMap.put(double.class, Double.class);
        primitiveMap.put(boolean.class, Boolean.class);
        primitiveMap.put(char.class, Character.class);
    }

    private static final String NUMBER_EXPRESSION = "#{int.random}";

    public static String getExpression(Class<?>[] types) {
        if (null == types) {
            return "[]";
        }
        int length = types.length;
        if (length == 0) {
            return "[]";
        }

        StringBuilder ex = new StringBuilder(length * 10);

        ex.append("[");
        for (Class type : types) {
            ex.append("\"");
            ex.append(toExpre(type));
            ex.append("\", ");
        }

        length = ex.length();
        if (length > 1) {
            ex.delete(length - 2, length);
        }

        ex.append("]");
        return ex.toString();
    }

    private static String toExpre(Class<?> type) {
        String expre = getSimpleExpre(type);
        if (null != expre) {
            return expre;
        }

        return toObjectExpre(type);
    }

    private static String getSimpleExpre(Class<?> type) {
        type = convertPrimitive(type);

        if (Number.class.isAssignableFrom(type)) {
            return NUMBER_EXPRESSION;
        }
        if (type == Boolean.TYPE) {
            return Boolean.TRUE.toString();
        }
        if (type == Character.TYPE || type == String.class) {
            return "";
        }
        if (type.isArray() || Collection.class.isAssignableFrom(type)) {
            return "[]";
        }
        if (type.isEnum()) {
            return "null";
        }
        return null;
    }

    private static Class<?> convertPrimitive(Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }
        Class<?> clazz = primitiveMap.get(type);
        if (null != clazz) {
            return clazz;
        }

        return type;
    }

    private static String toObjectExpre(Class<?> type) {

        StringJoiner json = new StringJoiner(", " , "{", "}");

        Field[] fields;
        Class<?> superclass = type.getSuperclass();
        while (superclass != null && superclass != Object.class) {
            fields = superclass.getDeclaredFields();
            append(json, fields);
            superclass = superclass.getSuperclass();
        }

        fields = type.getDeclaredFields();
        append(json, fields);
        return json.toString();
    }

    private static void append(StringJoiner json, Field[] fields) {
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            String name = field.getName();
            json.add("'" + name + "': '" + getSimpleExpre(field.getType()) + "'");
        }
    }

    public static String getExpression(String path, String[] param, String[] header) {
        return null;
    }

    public static void main(String[] args) {
        Class<?>[] classes;
        classes = new Class[3];
        classes[0] = List.class;
        classes[1] = FunctionInfo.class;
        classes[2] = String.class;
        String expression = getExpression(classes);
        System.out.println(expression);
    }
}
