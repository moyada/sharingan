package cn.moyada.sharingan.instrument.boost.common;

/**
 * 代理参数
 * @author xueyikang
 * @since 0.0.1
 **/
public class ProxyField {

    /**
     * 参数类型
     */
    private Class paramType;

    /**
     * 参数位置
     */
    private int paramIndex;

    /**
     * 参数名
     */
    private String paramName;

    public Class getParamType() {
        return paramType;
    }

    public void setParamType(Class paramType) {
        this.paramType = paramType;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public void setParamIndex(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
