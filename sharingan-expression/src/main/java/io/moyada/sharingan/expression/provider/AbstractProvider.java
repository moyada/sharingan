package io.moyada.sharingan.expression.provider;


import io.moyada.sharingan.infrastructure.enums.ConvertType;
import io.moyada.sharingan.infrastructure.util.ConvertUtil;

/**
 * 提供器
 */
public abstract class AbstractProvider implements ArgsProvider {

    /**
     * 转换类型
     */
    protected final ConvertType convertType;

    /**
     * 类型
     */
    protected final Class<?> paramType;

    /**
     * json值
     */
    protected final String value;

    public AbstractProvider(String value, Class<?> paramType) {
        this.value = value;
        this.paramType = paramType;
        this.convertType = ConvertUtil.getConvertType(paramType);
    }

    protected abstract String next();
}
