package cn.moyada.sharingan.core.supplier;

import cn.moyada.sharingan.common.util.TimeUtil;

import java.util.Random;

/**
 * 随机下标提供器
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public class RandomIndexSupplier extends AbstractIndexSupplier {

    private static final long BIT = 0x000000007fffL;

    private int prime = 0;

    public RandomIndexSupplier(int threshold) {
        super(threshold);
        this.prime = findPrime();
    }

    protected int findPrime() {
        int num, factor;
        for (num = threshold / 2; num > 0; num--) {
            if((num - 1) % 2 != 0) {
                continue;
            }
            for (factor = 2; factor < num; factor++) {
                if(num % factor == 0) {
                    break;
                }
            }
            if(factor != num - 1) {
                return num;
            }
        }
        return new Random().nextInt(threshold);
    }

    public int nextIndex() {
        return ((int) (TimeUtil.currentTimeMillis() & BIT) + prime++) & threshold;
    }
}
