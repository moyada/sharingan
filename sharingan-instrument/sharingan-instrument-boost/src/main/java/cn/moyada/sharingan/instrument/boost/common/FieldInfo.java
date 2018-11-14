package cn.moyada.sharingan.instrument.boost.common;

/**
 * 字段信息
 * @author xueyikang
 * @since 0.0.1
 **/
public class FieldInfo {

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 私有名
     */
    private String primitiveName;

    /**
     * 方法名
     */
    private String setMethodName;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getPrimitiveName() {
        return primitiveName;
    }

    public void setPrimitiveName(String primitiveName) {
        this.primitiveName = primitiveName;
    }

    public String getSetMethodName() {
        return setMethodName;
    }

    public void setSetMethodName(String setMethodName) {
        this.setMethodName = setMethodName;
    }
}
