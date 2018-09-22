package cn.moyada.sharingan.core.provider;

import cn.moyada.sharingan.core.support.DoubleRange;

/**
 * 随机数值提供器
 * @author xueyikang
 * @since 1.0
 **/
public class DoubleProvider extends RandomProvider implements ArgsProvider {

    private final String target;

    private final double base;
    private final double range;

    public DoubleProvider(String value, Class<?> paramType, DoubleRange doubleRange) {
        super(value, paramType);
        this.target = doubleRange.getTarget();
        this.base = doubleRange.getStart();
        this.range = doubleRange.getEnd() - doubleRange.getStart();
    }

    @Override
    public Object fetchNext() {
        Double data = random.nextDouble() * range + base;
        String newData = value.replace(target, data.toString());
        Object next = convert(newData, paramType);
        return next;
    }
}
