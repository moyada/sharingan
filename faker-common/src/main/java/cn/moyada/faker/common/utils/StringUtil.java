package cn.moyada.faker.common.utils;

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
}
