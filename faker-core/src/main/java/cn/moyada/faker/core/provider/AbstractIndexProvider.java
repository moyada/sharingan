package cn.moyada.faker.core.provider;

/**
 * 下标提供器
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public abstract class AbstractIndexProvider {

    protected final int threshold;

    public AbstractIndexProvider(int threshold) {
        this.threshold = threshold;
    }

    public abstract int nextIndex();
}
