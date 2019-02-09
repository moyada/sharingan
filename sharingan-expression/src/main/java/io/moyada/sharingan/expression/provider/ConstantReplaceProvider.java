package io.moyada.sharingan.expression.provider;

/**
 * 常量替换提供器
 * @author xueyikang
 * @since 0.0.1
 **/
public class ConstantReplaceProvider extends ReplacementProvider implements ArgsProvider {

    private Object constantValue;

    public ConstantReplaceProvider(String value, String target, Object constant, Class<?> paramType) {
        super(value, paramType, target);
        constantValue = constant;
        constantValue = super.fetchNext();
    }

    @Override
    public Object fetchNext() {
        return constantValue;
    }

    @Override
    protected String next() {
        return constantValue.toString();
    }
}
