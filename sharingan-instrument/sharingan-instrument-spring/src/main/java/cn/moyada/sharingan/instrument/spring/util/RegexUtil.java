package cn.moyada.sharingan.instrument.spring.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class RegexUtil {

    private static final String PROTOCOL_REGEX = "\\w+:\\\\\\\\";

    private static final Pattern PROTOCOL_PATTERN = Pattern.compile(PROTOCOL_REGEX);

    public static boolean isProtocol(String address) {
        Matcher matcher = PROTOCOL_PATTERN.matcher(address);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public static boolean isProtocol(String address, String protocol) {
        Matcher matcher = PROTOCOL_PATTERN.matcher(address);
        if (!matcher.find()) {
            return false;
        }
        String findProtocol = matcher.group();
        if (findProtocol.equals(protocol)) {
            return true;
        }
        return false;
    }
}
