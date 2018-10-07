package cn.moyada.sharingan.monitor.api;

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
}
