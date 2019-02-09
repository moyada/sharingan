package io.moyada.sharingan.monitor.api.entity;

/**
 * 协议类型
 */
public enum Protocol {

    DUBBO("dubbo"),
    SPRING_CLOUD("springcloud"),
    ;

    private String value;

    Protocol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
