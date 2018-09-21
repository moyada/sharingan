package cn.moyada.sharingan.core.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数表达式工具
 */
public class ExpressionUtil {

    private static final String expressionRegex = "\\$\\{[\\w\\-]+\\.[\\w\\-]+(\\.[\\w\\-]+)*\\}";

    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(expressionRegex);

    /**
     * 查询包含的表达式
     * @param value
     * @return
     */
    public static String findExpression(String value) {
        Matcher matcher = EXPRESSION_PATTERN.matcher(value);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group();
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
}
