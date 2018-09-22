package cn.moyada.sharingan.core.provider;

/**
 * 常量提供器
 */
public class ConstantProvider extends AbstractProvider implements ArgsProvider {

    /**
     * 常量值
     */
    private final Object constantValue;

    public ConstantProvider(String value, Class<?> paramType) {
        super(value, paramType);
        constantValue = convert(value, paramType);
    }

    @Override
    public Object fetchNext() {
        return constantValue;
    }
}
