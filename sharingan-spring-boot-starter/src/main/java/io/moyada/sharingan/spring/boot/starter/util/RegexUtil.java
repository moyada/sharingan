package io.moyada.sharingan.spring.boot.starter.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具
 * @author xueyikang
 * @since 0.0.1
 **/
public class RegexUtil {

    private static final String PARAM_REGEX_1 = "\\.\\w+";
    private static final String PARAM_REGEX_2 = "\\[\\w+\\]";

    private static final Pattern PARAM_PATTERN_1 = Pattern.compile(PARAM_REGEX_1);
    private static final Pattern PARAM_PATTERN_2 = Pattern.compile(PARAM_REGEX_2);

    /**
     * 校验参数名格式是否为 .xxx 或 [xxx]，返回去格式后名称
     * @param propertiesName
     * @return
     */
    public static String getParam(String propertiesName) {
        Matcher matcher = PARAM_PATTERN_1.matcher(propertiesName);
        if (matcher.matches()) {
            return propertiesName.substring(1);
        }
        matcher = PARAM_PATTERN_2.matcher(propertiesName);
        if (matcher.matches()) {
            return propertiesName.substring(1, propertiesName.length() - 1);
        }
        return null;
    }
}
