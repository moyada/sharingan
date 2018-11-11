package cn.moyada.sharingan.monitor.api;

public enum Protocol {

    DUBBO("dubbo"),
    SPRING_CLOUD("springcloud"),
    ;

    private String protocol;

    Protocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    static {
        Protocol[] protocols = Protocol.values();
        for (Protocol protocol : protocols) {
            String value = protocol.getProtocol().trim();
            if (value.equals("")) {
                throw new IllegalArgumentException("Protocol Error: " + protocol.name() +" protocol value can not be space.");
            }
            protocol.setProtocol(value);
        }
    }
}
