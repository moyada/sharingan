package cn.moyada.dubbo.faker.core.model.padding;

/**
 * @author xueyikang
 * @create 2018-04-03 16:51
 */
public abstract class Bit6Padding extends Bit5Padding {

    private volatile long l6;

    protected long getPadding() {
        return super.getPadding() + l6;
    }
}
