package cn.moyada.sharingan.core.provider;

/**
 * 常量替换提供器
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
