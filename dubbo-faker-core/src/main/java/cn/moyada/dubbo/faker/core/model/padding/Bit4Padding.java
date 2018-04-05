package cn.moyada.dubbo.faker.core.model.padding;

/**
 * @author xueyikang
 * @create 2018-04-03 16:51
 */
public abstract class Bit4Padding {

    private volatile long l1, l2, l3, l4;

    protected long getPadding() {
        return l1 + l2 + l3 + l4;
    }
}
