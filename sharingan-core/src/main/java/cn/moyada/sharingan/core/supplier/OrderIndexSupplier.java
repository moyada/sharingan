package cn.moyada.sharingan.core.supplier;

/**
 * 顺序下标提供器
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public class OrderIndexSupplier extends AbstractIndexSupplier {

    private int currentIndex;

    public OrderIndexSupplier(int threshold) {
        super(threshold);
        this.currentIndex = 0;
    }

    public int nextIndex() {
        return currentIndex & threshold;
    }
}
