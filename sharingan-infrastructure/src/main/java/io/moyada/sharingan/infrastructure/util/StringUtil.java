package io.moyada.sharingan.infrastructure.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

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

    public static String toString(Object[] array) {
        if(null == array) {
            return "[]";
        }
        int iMax = array.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(JsonUtil.toJson(array[i]));
            if (i == iMax) {
                return b.append(']').toString();
            }
            b.append(", ");
        }
    }

    public static String replace(String source, String target, String replacement) {
        return StringUtils.replaceOnce(source, target, replacement);
    }
}
