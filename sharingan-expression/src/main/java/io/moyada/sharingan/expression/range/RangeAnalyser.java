package io.moyada.sharingan.expression.range;


import io.moyada.sharingan.infrastructure.constant.NumberConstant;
import io.moyada.sharingan.infrastructure.util.NumberUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class RangeAnalyser {

    private static final String RANDOM = "random";
    private static final char SPLIT = '-';

    private static final String intRegex = "\\-?\\d+";
    private static final String doubleRegex = "\\-?\\d+\\.?\\d*";

    private static final Pattern INT_PATTERN = Pattern.compile(intRegex);

    private static final Pattern DOUBLE_PATTERN = Pattern.compile(doubleRegex);

    /**
     * 获取数字表达式范围
     * @param expression
     * @return
     */
    public static Range<Integer> findIntRange(String expression) {
        // 是否为随机策略
        if (expression.contains(RANDOM)) {
            return new NumberRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
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

        return new NumberRange(start, end);
    }

    /**
     * 获取浮点数表达式范围
     * @param expression
     * @return
     */
    public static Range<Double> findDoubleRange(String expression) {
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
            return new DecimalRange(NumberConstant.SMALL_DOUBLE, NumberConstant.BIG_DOUBLE, precision);
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

        return new DecimalRange(start, end, precision);
    }
}
