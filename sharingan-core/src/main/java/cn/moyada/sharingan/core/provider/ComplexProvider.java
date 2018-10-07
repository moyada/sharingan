package cn.moyada.sharingan.core.provider;

/**
 * 复合提供器
 * @author xueyikang
 * @since 0.0.1
 **/
public class ComplexProvider extends AbstractProvider implements ArgsProvider {

    private ArgsProvider[] argsProviders;

    public ComplexProvider(String value, Class<?> paramType, ArgsProvider[] argsProviders) {
        super(value, paramType);
        this.argsProviders = argsProviders;
    }

    @Override
    public Object fetchNext() {
        return convert(next(), paramType);
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
