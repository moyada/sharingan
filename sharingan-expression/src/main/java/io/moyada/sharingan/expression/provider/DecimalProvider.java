package io.moyada.sharingan.expression.provider;


import io.moyada.sharingan.expression.range.Range;
import io.moyada.sharingan.infrastructure.constant.NumberConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * 浮点数提供器
 * @author xueyikang
 * @since 0.0.1
 **/
public class DecimalProvider extends RandomProvider implements ArgsProvider {

    private final boolean huge;

    // 精度
    private final NumberFormat format;

    // 基数
    private final double base;

    // 范围
    private final double range;

    public DecimalProvider(String value, Class<?> paramType, String target, Range<Double> decimalRange) {
        super(value, paramType, target);
        double start = decimalRange.getStart();
        double end = decimalRange.getEnd();
        double top = end - start;

        this.format = getNumberFormat(decimalRange.getPrecision());
        this.huge = decimalRange.isHuge();
        if (huge) {
            this.range = end;
            this.base = NumberConstant.BIG_DOUBLE == start ? NumberConstant.BIG_DOUBLE : -start;
        }
        else {
            this.range = top;
            this.base = start;
        }
    }

    private NumberFormat getNumberFormat(int prscision) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        if (prscision > 9) {
            numberFormat.setMaximumFractionDigits(9);
        } else {
            numberFormat.setMaximumFractionDigits(prscision);
        }
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        numberFormat.setGroupingUsed(false);
        return numberFormat;
    }

    @Override
    protected String next() {
        double data;
        if (huge) {
            data = (random.nextDouble() * range) - (random.nextDouble() * base);
            return new BigDecimal(data).setScale(format.getMaximumFractionDigits(), RoundingMode.HALF_UP)
                    .toString();
        } else {
            data = random.nextDouble() * range + base;
            return format.format(data);
        }
    }
}
