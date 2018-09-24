package cn.moyada.sharingan.core.provider;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ConstantReplaceProvider extends ReplacementProvider implements ArgsProvider {

    /**
     * 常量值
     */
    private final Object constantValue;

    public ConstantReplaceProvider(String value, Class<?> paramType, String target) {
        super(value, paramType, target);
        constantValue = convert(value, paramType);
    }

    @Override
    protected String next() {
        return constantValue.toString();
    }
}
