package cn.moyada.sharingan.common.utils;

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
}
