package io.moyada.sharingan.expression.range;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Range<T extends Number> {

    /**
     * 是否为常数
     * @return
     */
    boolean isConstant();

    T getStart();

    T getEnd();

    int getPrecision();

    boolean isHuge();
}
