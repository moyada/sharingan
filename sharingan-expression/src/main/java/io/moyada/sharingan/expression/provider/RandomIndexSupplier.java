package io.moyada.sharingan.expression.provider;


import io.moyada.sharingan.infrastructure.util.TimeUtil;

/**
 * 随机下标提供器
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public class RandomIndexSupplier extends AbstractIndexSupplier {

    private static final long BIT = 0x000000007fffL;

    private final static int[] PRIME = {5, 7, 9 , 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};

    private final static int TOTAL = PRIME.length - 1;
    private int idx;

    public RandomIndexSupplier() {
        this.idx = 0;
    }

    public int nextIndex() {
        int index = (int) (TimeUtil.currentTimeMillis() & BIT) * PRIME[idx];
        prepareNext();
        return mod(index);
    }

    private void prepareNext() {
        this.idx = (idx + 1) & TOTAL;
    }
}
