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

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    static {
        RpcProtocol[] rpcProtocols = RpcProtocol.values();
        for (RpcProtocol rpcProtocol : rpcProtocols) {
            String value = rpcProtocol.getProtocol().trim();
            if (value.equals("")) {
                throw new IllegalArgumentException("RpcProtocol Error: " + rpcProtocol.name() +" protocol value can not be space.");
            }
            rpcProtocol.setProtocol(value);
        }
    }
}
