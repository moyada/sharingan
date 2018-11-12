package cn.moyada.sharingan.instrument.boost.common;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class FieldInfo {

    private String paramName;

    private String primitiveName;

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
