package io.moyada.sharingan.monitor.mysql.support;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class NameUtil {

    public static String getValue(Class clazz) {
        if (null == clazz) {
            return "NULL";
        }
        return "'" + clazz.getName() + "'";
    }

    public static String getValue(String value) {
        if (null == value) {
            return "NULL";
        }
        return "'" + value + "'";
    }

    private static boolean isEmpty(Object[] classes) {
        if (null == classes) {
            return true;
        }
        int length = classes.length;
        if (length == 0) {
            return true;
        }
        return false;
    }

    public static String getName(Class[] classes) {
        if (isEmpty(classes)) {
            return null;
        }

        StringBuilder name = new StringBuilder(classes.length * 20);
        for (Class clazz : classes) {
            name.append(clazz.getName()).append(",");
        }
        return name.deleteCharAt(name.length()-1).toString();
    }

    public static String getName(String[] names) {
        if (isEmpty(names)) {
            return null;
        }

        StringBuilder name = new StringBuilder(names.length * 10);
        for (String str : names) {
            name.append(str).append(",");
        }
        return name.deleteCharAt(name.length()-1).toString();
    }
}
