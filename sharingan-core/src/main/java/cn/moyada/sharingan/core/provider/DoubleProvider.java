package cn.moyada.sharingan.core.provider;

import cn.moyada.sharingan.common.constant.NumberConstant;
import cn.moyada.sharingan.core.support.DoubleRange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * 浮点数提供器
 * @author xueyikang
 * @since 0.0.1
 **/
public class DoubleProvider extends RandomProvider implements ArgsProvider {

    private final boolean huge;

    // 精度
    private final NumberFormat format;

    // 基数
    private final double base;

    // 范围
    private final double range;

    public DoubleProvider(String value, Class<?> paramType, DoubleRange doubleRange) {
        super(value, paramType, doubleRange.getTarget());

        format = NumberFormat.getNumberInstance();
        if (doubleRange.getPrecision() > 9) {
            format.setMaximumFractionDigits(9);
        } else {
            format.setMaximumFractionDigits(doubleRange.getPrecision());
        }
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setGroupingUsed(false);

        double top = doubleRange.getEnd() - doubleRange.getStart();

        // 是否大数值
        boolean huge = top >= NumberConstant.BIG_DOUBLE / 100
                && doubleRange.getStart() - doubleRange.getEnd() <= NumberConstant.SMALL_DOUBLE / 100;

        if (huge) {
            this.range = doubleRange.getEnd();
            this.base = NumberConstant.BIG_DOUBLE == doubleRange.getStart()
                    ? NumberConstant.BIG_DOUBLE : -doubleRange.getStart();
            this.huge = true;
        }
        else {
            this.range = top;
            this.base = doubleRange.getStart();
            this.huge = false;
        }
    }

    @Override
    protected String next() {
        double data;
        if (huge) {
            data = random.nextDouble() * range - random.nextDouble() * base;
            return new BigDecimal(data).setScale(format.getMaximumFractionDigits(), RoundingMode.HALF_UP).toString();
        } else {
            data = random.nextDouble() * range + base;
            return format.format(data);
        }
    }
}
