package cn.moyada.sharingan.instrument.boost.common;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ProxyField {

    private Class paramType;

    private int paramIndex;

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
