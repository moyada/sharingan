package io.moyada.sharingan.spring.boot.starter.util;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class NameUtil {

    public static boolean isEmpty(String str) {
        if (null == str) {
            return true;
        }

        byte[] bytes = str.getBytes();
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            if (!Character.isSpaceChar(bytes[i])) {
                return false;
            }
        }
        return true;
    }

    public static String toString(String[] strs, char split) {
        if (strs == null) {
            return null;
        }
        int length = strs.length;
        if (length == 0) {
            return null;
        }
        StringBuilder value = new StringBuilder(length * 4);
        for (String str : strs) {
            value.append(str).append(split);
        }
        return value.deleteCharAt(value.length() - 1).toString();
    }

    /**
     * 获取代理名
     * @param className
     * @return
     */
    public static String getProxyName(String className) {
        int index = className.lastIndexOf('.');
        String simpleName = -1 == index ? className : className.substring(index + 1);
        if (!simpleName.contains("$")) {
            return className + "$_Proxy";
        }
        return className + "_Proxy";
    }

    /**
     * 返回标准 set 方法
     * @param simpleName
     * @return
     */
    public static String getSetFunction(String simpleName) {
        return "set" + toUpperCapitalName(simpleName);
    }

    /**
     * 生成私有名
     * @param simpleName
     * @return
     */
    public static String genPrivateName(String simpleName) {
        return "_" + toLowerCapitalName(simpleName) + "_";
    }

    /**
     * 首字母小写
     * @param simpleName
     * @return
     */
    public static String toLowerCapitalName(String simpleName) {
        char c = simpleName.charAt(0);
        c = Character.toLowerCase(c);
        return c + simpleName.substring(1);
    }

    /**
     * 首字母大写
     * @param simpleName
     * @return
     */
    public static String toUpperCapitalName(String simpleName) {
        char c = simpleName.charAt(0);
        c = Character.toUpperCase(c);
        return c + simpleName.substring(1);
    }
}
