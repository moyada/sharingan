package cn.moyada.dubbo.faker.core.model.padding;

/**
 * @author xueyikang
 * @create 2018-04-03 16:50
 */
public abstract class Bit7Padding extends Bit6Padding {

    private volatile long l7;

    protected long getPadding() {
        return super.getPadding() + l7;
    }
}
