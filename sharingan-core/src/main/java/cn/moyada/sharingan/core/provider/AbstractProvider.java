package cn.moyada.sharingan.core.provider;

import cn.moyada.sharingan.common.enums.ConvertType;
import cn.moyada.sharingan.common.util.ConvertUtil;
import cn.moyada.sharingan.common.util.JsonUtil;

/**
 * 提供器
 */
public abstract class AbstractProvider implements ArgsProvider {

    /**
     * 转换类型
     */
    private final ConvertType convertType;

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

    /**
     * 根据参数类型转换
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> T convert(String value, Class<T> paramType) {
        T data;
        switch (convertType) {
            case LIST:
                data = (T) JsonUtil.toList(value, Object.class);
                break;
//            case ARRAY:
//                data = JsonUtil.toArray(json, paramType);
//                break;
            default:
                data = JsonUtil.toObject(value, paramType);
        }

        return data;
    }
}
