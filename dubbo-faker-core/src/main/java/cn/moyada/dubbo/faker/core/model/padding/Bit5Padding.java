package cn.moyada.dubbo.faker.core.model.padding;

/**
 * @author xueyikang
 * @create 2018-04-03 16:51
 */
public abstract class Bit5Padding extends Bit4Padding {

    private volatile long l5;

    protected long getPadding() {
        return l5;
    }
}
