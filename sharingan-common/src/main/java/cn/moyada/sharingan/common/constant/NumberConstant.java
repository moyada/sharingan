package cn.moyada.sharingan.common.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class NumberConstant {

    public static final double BIG_DOUBLE;
    public static final double SMALL_DOUBLE;

    static {
        String value = StringUtils.repeat('9', 15);
        String data = StringUtils.repeat('8', 230);
        String precision = StringUtils.repeat('7', 5);
        BIG_DOUBLE = Double.valueOf(value + data + "." + precision);
        SMALL_DOUBLE = Double.valueOf("-" + value);
    }
}
