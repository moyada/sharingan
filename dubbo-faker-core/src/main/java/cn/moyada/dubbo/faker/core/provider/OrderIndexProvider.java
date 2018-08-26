package cn.moyada.dubbo.faker.core.provider;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 顺序下标提供器
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public class OrderIndexProvider extends AbstractIndexProvider {

    private final AtomicInteger count;

    public OrderIndexProvider(int threshold) {
        super(threshold);
        this.count = new AtomicInteger(0);
    }

    public int nextIndex() {
        return count.getAndIncrement();
    }
}
