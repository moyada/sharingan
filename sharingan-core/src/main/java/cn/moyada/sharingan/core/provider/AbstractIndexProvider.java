package cn.moyada.sharingan.core.provider;

import cn.moyada.sharingan.common.exception.InitializeInvokerException;
import cn.moyada.sharingan.common.utils.NumberUtil;

/**
 * 下标提供器
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public abstract class AbstractIndexProvider implements IndexProvider {

    /**
     * 2的N次幂-1
     */
    protected final int threshold;

    public AbstractIndexProvider(int threshold) {
        threshold = NumberUtil.getIdempotent(threshold);
        if (threshold < 2) {
            throw new InitializeInvokerException("data size too little.");
        }
        this.threshold = threshold - 1;
    }
}
