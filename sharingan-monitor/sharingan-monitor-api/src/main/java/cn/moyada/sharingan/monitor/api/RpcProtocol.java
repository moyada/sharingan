package cn.moyada.sharingan.monitor.api;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public enum RpcProtocol {

    DUBBO("dubbo"),
    SPRING_CLOUD("springcloud"),
    ;

    private String protocol;

    RpcProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }
}
