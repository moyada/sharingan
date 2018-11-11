package cn.moyada.sharingan.spring.parser.util;

/**
 * @author xueyikang
 * @since 1.0
 **/
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

    public static String getSimpleName(String className) {
        int index = className.lastIndexOf('.');
        if (index == -1) {
            return className;
        }
        return className.substring(index + 1);
    }
}
