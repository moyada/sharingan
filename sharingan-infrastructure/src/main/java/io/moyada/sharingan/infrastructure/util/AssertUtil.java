package io.moyada.sharingan.infrastructure.util;


public class AssertUtil {

    private static final String POSITIVE = "value must be positive";

    public static void checkoutNotNull(Object obj, String exceptionMsg) {
        if (null == obj) {
            throw new NullPointerException(exceptionMsg);
        }
    }

    public static void checkoutPositive(int value) {
        if (value < 1) {
            throw new IllegalArgumentException(POSITIVE);
        }
    }
}
