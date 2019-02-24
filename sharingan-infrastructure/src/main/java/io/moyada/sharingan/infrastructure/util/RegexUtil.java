package io.moyada.sharingan.infrastructure.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class RegexUtil {

    private static final String pathRegex = "\\{\\w+\\}";

    private static final Pattern PATH_PATTERN = Pattern.compile(pathRegex);

    private static final String fieldRegex = "\\w+:";

    private static final Pattern fieldPattern = Pattern.compile(fieldRegex);

    /**
     * 获取请求路径动态参数
     * @param path
     * @return
     */
    public static List<String> findPathVariable(String path) {
        Matcher matcher = PATH_PATTERN.matcher(path);
        List<String> params = new ArrayList<>();
        String data;
        while (matcher.find()) {
            data = matcher.group();
            params.add(data.substring(1, data.length() - 1));
        }
        return params;
    }

    public static String replaceJson(String json) {
        Matcher matcher = fieldPattern.matcher(json);
        String data;
        while (matcher.find()) {
            data = matcher.group();
            json = json.replace(data, getReplacement(data));
        }
        return json;
    }

    private static String getReplacement(String data) {
        String substring = data.substring(0, data.length() - 1);
        return "\"" + substring + "\":";
    }
}
