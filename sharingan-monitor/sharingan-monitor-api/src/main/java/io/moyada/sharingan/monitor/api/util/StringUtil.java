package io.moyada.sharingan.monitor.api.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class StringUtil {

    public static String concat(CharSequence split, Object... values) {
        return Stream.of(values).map(Object::toString).collect(Collectors.joining(split));
//        StringBuilder name = new StringBuilder(values.length * 6);
//        for (Object value : values) {
//            name.append(value).append(split);
//        }
//        return name.deleteCharAt(name.length()-1).toString();
    }
}
