package io.moyada.sharingan.expression;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ExpressionAnalyser {

    // ${app.domain}
    private static final String expressionRegex = "\\$\\{[\\w\\-]+\\.[\\w\\-]+(\\.[\\w\\-]+)*\\}";

    // #{int.1-365} || #{int.random}
    private static final String intRangeRegex = "\\#\\{int\\.((\\-?\\d+\\-\\-?\\d+)|random)\\}";

    // #{double.20-60.5} || #{double.random}
    private static final String doubleRangeRegex = "\\#\\{double(\\[\\d+\\])?\\.((\\-?\\d+\\.?\\d*\\-\\-?\\d+\\.?\\d*)|random)\\}";

    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(expressionRegex);

    private static final Pattern INT_RANGE_PATTERN = Pattern.compile(intRangeRegex);

    private static final Pattern DOUBLE_RANGE_PATTERN = Pattern.compile(doubleRangeRegex);

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
    public static String findInt(String value) {
        return findRegex(INT_RANGE_PATTERN, value);
    }

    /**
     * 查询浮点数表达式
     * @param value
     * @return
     */
    public static String findDouble(String value) {
        return findRegex(DOUBLE_RANGE_PATTERN, value);
    }

    /**
     * 获取表达式路由
     * @param expression
     * @return
     */
    public static String[] findRoute(String expression) {
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
