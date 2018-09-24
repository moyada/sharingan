package cn.moyada.sharingan.core.provider;

import cn.moyada.sharingan.core.support.DoubleRange;

import java.text.NumberFormat;

/**
 * 随机数值提供器
 * @author xueyikang
 * @since 1.0
 **/
public class DoubleProvider extends RandomProvider implements ArgsProvider {

    private final boolean huge;

    private final NumberFormat format;

    private final double base;
    private final double range;

    public DoubleProvider(String value, Class<?> paramType, DoubleRange doubleRange) {
        super(value, paramType, doubleRange.getTarget());

        format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(doubleRange.getPrecision());

        double top = doubleRange.getEnd() - doubleRange.getStart();
        boolean huge = doubleRange.getEnd() >= 0;
        huge = huge && top < 0;

        if (huge) {
            this.range = doubleRange.getEnd();
            this.base = doubleRange.getStart();
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
        } else {
            data = random.nextDouble() * range + base;
        }
        return format.format(data);
    }
}
