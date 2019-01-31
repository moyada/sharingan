package io.moyada.sharingan.expression.provider;

/**
 * 下标提供器
 */
public interface IndexSupplier {

    /**
     * 获取下一个下标
     * @return
     */
    int nextIndex();

    void setTotal(int total);
}
