package cn.moyada.sharingan.core.support;

/**
 * 顺序下标提供器
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public class OrderIndexProvider extends AbstractIndexProvider {

    private int currentIndex;

    public OrderIndexProvider(int threshold) {
        super(threshold);
        this.currentIndex = 0;
    }

    public int nextIndex() {
        return currentIndex & threshold;
    }
}
