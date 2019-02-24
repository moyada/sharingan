package io.moyada.sharingan.expression.provider;

import io.moyada.sharingan.infrastructure.util.ConvertUtil;

/**
 * 复合提供器
 * @author xueyikang
 * @since 0.0.1
 **/
public class CompositeProvider extends AbstractProvider implements ArgsProvider {

    private ArgsProvider[] argsProviders;

    public CompositeProvider(String value, Class<?> paramType, ArgsProvider[] argsProviders) {
        super(value, paramType);
        this.argsProviders = argsProviders;
    }

    @Override
    public Object fetchNext() {
        return ConvertUtil.convert(convertType, next(), paramType);
    }

    @Override
    public String replace(String source) {
        return next();
    }

    @Override
    protected String next() {
        String data = argsProviders[0].replace(value);

        int length = argsProviders.length;
        for (int index = 1; index < length; index++) {
            data = argsProviders[index].replace(data);
        }
        return data;
    }
}
