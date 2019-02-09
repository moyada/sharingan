package io.moyada.sharingan.expression.provider;

import io.moyada.sharingan.infrastructure.util.ConvertUtil;
import io.moyada.sharingan.infrastructure.util.StringUtil;

/**
 * 替换提供器
 * @author xueyikang
 * @since 0.0.1
 **/
public abstract class ReplacementProvider extends AbstractProvider implements ArgsProvider {

    // 替换目标
    private final String target;

    private final boolean nonReplace;

    public ReplacementProvider(String value, Class<?> paramType, String target) {
        super(value, paramType);
        this.target = target;
        this.nonReplace = value.equals(target);
    }

    @Override
    public Object fetchNext() {
        return ConvertUtil.convert(convertType, replace(value), paramType);
    }

    @Override
    public String replace(String source) {
        String next = next();
        if (nonReplace) {
            return next;
        }
        return StringUtil.replace(source, target, next);
    }
}
