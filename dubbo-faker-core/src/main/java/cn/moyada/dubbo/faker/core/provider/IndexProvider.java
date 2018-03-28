package cn.moyada.dubbo.faker.core.provider;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public class IndexProvider {

    private static final long BIT = 0x000000007fffL;

    private final int threshold;
    private int prime;
    private final boolean random;

    private final AtomicInteger count;

    public IndexProvider(int threshold, boolean random) {
        this.threshold = threshold;
        this.random = random;
        this.count = new AtomicInteger(0);
        if(random) {
            this.prime = findPrime();
        }
    }

    private int findPrime() {
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
        if(random) {
            return ((int) (System.nanoTime() & BIT) + prime) % threshold;
        }
        else {
            return count.getAndIncrement();
        }
    }
}
