package io.moyada.sharingan.expression.provider;


import io.moyada.sharingan.expression.range.Range;

/**
 * 整数提供器
 * @author xueyikang
 * @since 0.0.1
 **/
public class NumberProvider extends RandomProvider implements ArgsProvider {

    private final boolean huge;

    // 基数
    private final int base;

    // 范围
    private final int range;

    public NumberProvider(String value, Class<?> paramType, String target, Range<Integer> numberRange) {
        super(value, paramType, target);
        int start = numberRange.getStart();
        int end = numberRange.getEnd();

        this.huge = numberRange.isHuge();
        if (huge) {
            if (end == 0) {
                end++;
                start++;
            }
            this.range = plus(end);
            this.base = Integer.MIN_VALUE == start ? Integer.MAX_VALUE : -start;
        }
        else {
            this.range = plus(end - start);
            this.base = start;
        }
    }

    private int plus(int value) {
        if (value == Integer.MAX_VALUE) {
            return value;
        }
        return value+1;
    }

    @Override
    protected String next() {
        int data;
        if (huge) {
            data = random.nextInt(range) - random.nextInt(base);
        } else {
            data = random.nextInt(range) + base;
        }
        return Integer.toString(data);
    }
}
