package cn.moyada.sharingan.agent.spring.util;

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

    public static String getSimpleName(String path) {
        int index = path.lastIndexOf('.');
        if (-1 == index) {
            return path;
        }
        return path.substring(index + 1);
    }
}
