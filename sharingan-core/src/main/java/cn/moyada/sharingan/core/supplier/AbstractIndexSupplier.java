package cn.moyada.sharingan.core.supplier;

import cn.moyada.sharingan.common.exception.InitializeInvokerException;
import cn.moyada.sharingan.common.util.NumberUtil;

/**
 * 下标提供器
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public abstract class AbstractIndexSupplier implements IndexSupplier {

    /**
     * 2的N次幂-1
     */
    protected final int threshold;

    public AbstractIndexSupplier(int threshold) {
        threshold = NumberUtil.getIdempotent(threshold);
        if (threshold < 2) {
            throw new InitializeInvokerException("data quantity is too small.");
        }
        this.threshold = threshold - 1;
    }
}
