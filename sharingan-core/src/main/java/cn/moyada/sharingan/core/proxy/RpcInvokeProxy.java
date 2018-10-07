package cn.moyada.sharingan.core.proxy;

import cn.moyada.sharingan.common.exception.InitializeInvokerException;
import cn.moyada.sharingan.rpc.api.invoke.InvokeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 协议代理器
 * @author xueyikang
 * @since 0.0.1
 */
@Component
public class RpcInvokeProxy {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取协议对应代理
     * @param protocol
     * @return
     */
    public InvokeProxy getInvoke(String protocol) {
        InvokeProxy invokeProxy;
        try {
            invokeProxy = applicationContext.getBean(protocol.concat("Invoke").intern(), InvokeProxy.class);
        } catch (Exception e) {
            throw new InitializeInvokerException("cannot find InvokeProxy by " + protocol);
        }
        return invokeProxy;
    }
}
