package io.moyada.sharingan.monitor.api.util;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class StringUtil {

    public static String concat(char split, Object... values) {
        StringBuilder name = new StringBuilder(values.length * 6);
        for (Object value : values) {
            name.append(value).append(split);
        }
        return name.deleteCharAt(name.length()-1).toString();
    }
}
