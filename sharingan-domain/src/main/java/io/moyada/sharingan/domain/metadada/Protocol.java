package io.moyada.sharingan.domain.metadada;

/**
 * @author xueyikang
 * @since 1.0
 **/
public enum Protocol {

    Dubbo("dubbo", Mode.CLASS),
    SpringCloud("springcloud", Mode.HTTP),
    ;

    private String value;

    private Mode specialMode;

    public enum Mode {
        CLASS, HTTP
    }

    Protocol(String value, Mode specialMode) {
        this.value = value;
        this.specialMode = specialMode;
    }

    public Mode getMode() {
        return specialMode;
    }

    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    public boolean isClass() {
        return null != specialMode && specialMode == Mode.CLASS;
    }

    public boolean isHttp() {
        return null != specialMode && specialMode == Mode.HTTP;
    }

    /**
     * 校验协议
     */
    static {
        Protocol[] protocols = Protocol.values();
        for (Protocol protocol : protocols) {
            String value = protocol.getValue();
            if (value == null) {
                throw new IllegalArgumentException("Protocol Error: " + protocol.name() +" protocol value can not be null.");
            }
            value = value.trim();
            if (value.isEmpty()) {
                throw new IllegalArgumentException("Protocol Error: " + protocol.name() +" protocol value can not be empty.");
            }
            protocol.setValue(value);
        }
    }
}
