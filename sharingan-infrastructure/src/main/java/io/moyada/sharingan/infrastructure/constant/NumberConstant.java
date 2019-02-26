package io.moyada.sharingan.infrastructure.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class NumberConstant {

    public static final double BIG_DOUBLE;
    public static final double SMALL_DOUBLE;

    static {
        String value = StringUtils.repeat('9', 128)
                + "."
                + StringUtils.repeat('9', 32);
        BIG_DOUBLE = Double.valueOf(value);
        SMALL_DOUBLE = -Double.valueOf(value);
    }
}
