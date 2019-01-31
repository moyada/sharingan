package io.moyada.sharingan.expression.range;

/**
 * 整数范围策略
 * @author xueyikang
 * @since 0.0.1
 **/
public class NumberRange implements Range<Integer> {

    // 起始值
    private int start;

    // 结束值
    private int end;

    public NumberRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean isConstant() {
        return start == end;
    }

    @Override
    public Integer getStart() {
        return start;
    }

    @Override
    public Integer getEnd() {
        return end;
    }

    @Override
    public boolean isHuge() {
        // 当上限-下限发生溢出时
        return end >= 0 && end - start < 0;
    }

    @Override
    public int getPrecision() {
        return 0;
    }
}
