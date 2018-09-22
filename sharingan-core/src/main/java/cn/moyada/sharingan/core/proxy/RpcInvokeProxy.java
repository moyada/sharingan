package cn.moyada.sharingan.core.proxy;

import cn.moyada.sharingan.common.utils.AssertUtil;
import cn.moyada.sharingan.rpc.api.invoke.InvokeProxy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 协议代理器
 * @author xueyikang
 * @since 1.0
 */
@Component
public class RpcInvokeProxy implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 获取协议对应代理
     * @param protocol
     * @return
     */
    public InvokeProxy getInvoke(String protocol) {
        InvokeProxy invokeProxy = applicationContext.getBean(protocol, InvokeProxy.class);
        AssertUtil.checkoutNotNull(invokeProxy, "cannot find InvokeProxy by " + protocol);
        return invokeProxy;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
