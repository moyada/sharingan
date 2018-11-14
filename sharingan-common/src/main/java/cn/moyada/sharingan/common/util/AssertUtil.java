package cn.moyada.sharingan.common.util;

import cn.moyada.sharingan.common.exception.InitializeInvokerException;

public class AssertUtil {

    public static void checkoutNotNull(Object obj, String exceptionMsg) {
        if (null == obj) {
            throw new InitializeInvokerException(exceptionMsg);
        }
    }
}
