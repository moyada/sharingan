package io.moyada.sharingan.expression;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xueyikang
 * @since 1.0
 **/
class ExpressionAnalyser {

    // ${app.domain}
    private static final String EXPRESSION_REGEX = "\\$\\{[\\w\\-]+\\.[\\w\\-]+(\\.[\\w\\-]+)*\\}";
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(EXPRESSION_REGEX);

    // #{int.1-365} || #{int.random}
    private static final String INT_RANGE_REGEX = "\\#\\{int\\.((\\-?\\d+\\-\\-?\\d+)|random)\\}";
    private static final Pattern INT_RANGE_PATTERN = Pattern.compile(INT_RANGE_REGEX);

    // #{double.20-60.5} || #{double.random}
    private static final String DOUBLE_RANGE_REGEX = "\\#\\{double(\\[\\d+\\])?\\.((\\-?\\d+\\.?\\d*\\-\\-?\\d+\\.?\\d*)|random)\\}";
    private static final Pattern DOUBLE_RANGE_PATTERN = Pattern.compile(DOUBLE_RANGE_REGEX);

    /**
     * 查询参数表达式
     * @param value
     * @return
     */
    public static String findExpression(String value) {
        return findRegex(EXPRESSION_PATTERN, value);
    }

    /**
     * 查询数字表达式
     * @param value
     * @return
     */
    static String findInt(String value) {
        return findRegex(INT_RANGE_PATTERN, value);
    }

    /**
     * 查询浮点数表达式
     * @param value
     * @return
     */
    static String findDouble(String value) {
        return findRegex(DOUBLE_RANGE_PATTERN, value);
    }

    /**
     * 获取表达式路由
     * @param expression
     * @return
     */
    static String[] findRoute(String expression) {
        if (!expression.contains(".")) {
            return null;
        }
        return expression.substring(2, expression.length()-1).split("\\.");
    }

    /**
     * 查询包含正则
     * @param pattern
     * @param value
     * @return
     */
    private static String findRegex(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group();
    }
}
