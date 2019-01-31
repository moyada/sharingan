package io.moyada.sharingan.expression.provider;

import io.moyada.sharingan.infrastructure.enums.ConvertType;
import io.moyada.sharingan.infrastructure.util.ConvertUtil;

/**
 * 常量提供器
 */
public class ConstantProvider implements ArgsProvider {

    private final Object constantValue;

    public ConstantProvider(String value, Class<?> paramType) {
        ConvertType convertType = ConvertUtil.getConvertType(paramType);
        this.constantValue = ConvertUtil.convert(convertType, value, paramType);
    }

    @Override
    public Object fetchNext() {
        return constantValue;
    }

    @Override
    public String replace(String source) {
        throw new UnsupportedOperationException();
    }
}
