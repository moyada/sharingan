package io.moyada.sharingan.expression.range;

import io.moyada.sharingan.infrastructure.constant.NumberConstant;

/**
 * 浮点数范围策略
 * @author xueyikang
 * @since 0.0.1
 **/
public class DecimalRange implements Range<Double> {

    // 起始值
    private double start;

    // 结束值
    private double end;

    // 精确小数位
    private int precision;

    public DecimalRange(double start, double end, int precision) {
        this.start = start;
        this.end = end;
        this.precision = precision;
    }

    @Override
    public boolean isConstant() {
        return start == end;
    }

    @Override
    public Double getStart() {
        return start;
    }

    @Override
    public Double getEnd() {
        return end;
    }

    @Override
    public boolean isHuge() {
        double diff = end - start;
        if (diff < 0) {
            return true;
        }
        return diff >= NumberConstant.BIG_DOUBLE * 2;
    }

    @Override
    public int getPrecision() {
        return precision;
    }

}
