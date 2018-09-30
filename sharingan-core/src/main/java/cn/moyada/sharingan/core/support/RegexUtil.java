package cn.moyada.sharingan.core.support;

import cn.moyada.sharingan.common.constant.NumberConstant;
import cn.moyada.sharingan.common.utils.NumberUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数表达式工具
 */
public class RegexUtil {

    private static final String RANDOM = "random";
    private static final char SPLIT = '-';

    private static final String expressionRegex = "\\$\\{[\\w\\-]+\\.[\\w\\-]+(\\.[\\w\\-]+)*\\}";

    private static final String intRangeRegex = "\\#\\{int\\.((\\-?\\d+\\-\\-?\\d+)|random)\\}";
    private static final String intRegex = "\\-?\\d+";

    private static final String doubleRangeRegex = "\\#\\{double(\\[\\d+\\])?\\.((\\-?\\d+\\.?\\d*\\-\\-?\\d+\\.?\\d*)|random)\\}";
    private static final String doubleRegex = "\\-?\\d+\\.?\\d*";

    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(expressionRegex);

    private static final Pattern INT_RANGE_PATTERN = Pattern.compile(intRangeRegex);
    private static final Pattern INT_PATTERN = Pattern.compile(intRegex);

    private static final Pattern DOUBLE_RANGE_PATTERN = Pattern.compile(doubleRangeRegex);
    private static final Pattern DOUBLE_PATTERN = Pattern.compile(doubleRegex);

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
     * 获取数字表达式范围
     * @param expression
     * @return
     */
    public static IntRange findIntRange(String expression) {
        // 是否为随机策略
        if (expression.contains(RANDOM)) {
            return new IntRange(expression, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        Matcher matcher = INT_PATTERN.matcher(expression);

        // 获取起始值
        if (!matcher.find()) {
            throw new RangeException("cannot find any range of int.");
        }
        String m1 = matcher.group();
        Integer start = NumberUtil.toInt(m1);
        if (null == start) {
            throw new RangeException("value overflow, " + m1);
        }

        // 获取结束值
        if (!matcher.find()) {
            throw new RangeException("cannot find any range of int.");
        }
        String m2 = matcher.group();
        Integer end = NumberUtil.toInt(m2);
        if (null == end) {
            throw new RangeException("value overflow, " + m2);
        }

        int split = expression.indexOf(m2) - 1;
        if (expression.charAt(split) != SPLIT) {
            end = -end;
        }

        if (start > end) {
            throw new RangeException("range error, " + expression);
        }

        return new IntRange(expression, start, end);
    }

    /**
     * 获取浮点数表达式范围
     * @param expression
     * @return
     */
    public static DoubleRange findDoubleRange(String expression) {
        // 获取小数位
        int precision;
        int squareL = expression.indexOf('[');
        int squareR = -1;
        if (-1 == squareL) {
            precision = 3;
        } else {
            squareR = expression.indexOf(']');
            if (-1 == squareR) {
                precision = 3;
            } else {
                Integer prec = NumberUtil.toInt(expression.substring(squareL+1, squareR));
                precision = null == prec ? 3 : prec;
            }
        }

        // 是否为随机策略
        if (expression.contains(RANDOM)) {
            return new DoubleRange(expression, precision, NumberConstant.SMALL_DOUBLE, NumberConstant.BIG_DOUBLE);
        }

        String find;
        if (-1 == squareR) {
            find = expression;
        } else {
            find = expression.substring(squareR + 2);
        }

        Matcher matcher = DOUBLE_PATTERN.matcher(find);
        // 获取起始值
        if (!matcher.find()) {
            throw new RangeException("cannot find any range of double.");
        }
        String m1 = matcher.group();
        Double start = NumberUtil.toDouble(m1);
        if (null == start) {
            throw new RangeException("value overflow, " + m1);
        }

        // 获取结束值
        if (!matcher.find()) {
            throw new RangeException("cannot find any range of double.");
        }
        String m2 = matcher.group();
        Double end = NumberUtil.toDouble(m2);
        if (null == end) {
            throw new RangeException("value overflow, " + m2);
        }
        int split = expression.indexOf(m2) - 1;
        if (expression.charAt(split) != SPLIT) {
            end = -end;
        }

        if (start > end) {
            throw new RangeException("range error, " + expression);
        }

        return new DoubleRange(expression, precision, start, end);
    }
}
