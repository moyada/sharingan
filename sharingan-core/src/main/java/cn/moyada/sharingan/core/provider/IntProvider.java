package cn.moyada.sharingan.core.provider;

import cn.moyada.sharingan.core.support.IntRange;

/**
 * 随机数值提供器
 * @author xueyikang
 * @since 1.0
 **/
public class IntProvider extends RandomProvider implements ArgsProvider {

    private final String target;

    private final int base;
    private final int range;

    public IntProvider(String value, Class<?> paramType, IntRange intRange) {
        super(value, paramType);
        this.target = intRange.getTarget();
        this.base = intRange.getStart();
        this.range = intRange.getEnd() - intRange.getStart();
    }

    @Override
    public Object fetchNext() {
        Integer data = random.nextInt(range) + base;
        String newData = value.replace(target, data.toString());
        Object next = convert(newData, paramType);
        return next;
    }
}
