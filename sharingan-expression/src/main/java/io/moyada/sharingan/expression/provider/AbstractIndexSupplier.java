package io.moyada.sharingan.expression.provider;


import io.moyada.sharingan.infrastructure.util.NumberUtil;


/**
 * 下标提供器
 * @author xueyikang
 * @create 2018-03-28 05:17
 */
public abstract class AbstractIndexSupplier implements IndexSupplier {

    /**
     * 2的N次幂-1
     */
    private int threshold;

    private boolean idempotent;

    @Override
    public void setTotal(int total) {
        total = total -1;
        if (total < 100) {
            this.threshold = total;
            this.idempotent = false;
            return;
        }
        int idempotent = NumberUtil.getIdempotent(total);
        if ((total - idempotent) < 50) {
            this.threshold = idempotent;
            this.idempotent = true;
        } else {
            this.threshold = total;
            this.idempotent = false;
        }
    }

    int mod(int index) {
        if (idempotent) {
            return index & threshold;
        } else {
            return index % threshold;
        }
    }
}
