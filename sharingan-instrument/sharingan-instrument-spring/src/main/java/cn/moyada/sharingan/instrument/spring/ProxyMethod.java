package cn.moyada.sharingan.instrument.spring;


import cn.moyada.sharingan.monitor.api.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Listener(value = "test", protocol = RpcProtocol.DUBBO)
public class ProxyMethod {

    private String methodName;

    private String[] proxyParams;

    @Catch
    public String getMethodName() {
        return methodName;
    }

    @Catch
    public void setMethodName(@Rename("test") String methodName) {
        this.methodName = methodName;
    }

    @Catch
    public String[] getProxyParams() {
        return proxyParams;
    }

    @Catch
    public void setProxyParams(@Exclusive String[] proxyParams) {
        this.proxyParams = proxyParams;
    }

    public static void main(String[] args) {
        ProxyMethod proxyMethod = new ProxyMethod();
        proxyMethod.setMethodName("haha");
        proxyMethod.setProxyParams(new String[]{"hehe", "xixi"});
    }
}
