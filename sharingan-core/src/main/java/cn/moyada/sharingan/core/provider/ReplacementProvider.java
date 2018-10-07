package cn.moyada.sharingan.core.provider;

import org.apache.commons.lang3.StringUtils;

/**
 * 替换提供器
 * @author xueyikang
 * @since 0.0.1
 **/
public abstract class ReplacementProvider extends AbstractProvider implements ArgsProvider {

    protected final String target;

    public ReplacementProvider(String value, Class<?> paramType, String target) {
        super(value, paramType);
        this.target = target;
    }

    @Override
    public Object fetchNext() {
        String newValue = replace(value);
        return convert(newValue, paramType);
    }

    @Override
    public String replace(String source) {
        return StringUtils.replaceOnce(source, target, next());
    }
}
