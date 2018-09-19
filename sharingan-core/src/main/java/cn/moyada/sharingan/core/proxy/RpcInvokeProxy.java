package cn.moyada.sharingan.core.proxy;

import cn.moyada.sharingan.common.utils.AssertUtil;
import cn.moyada.sharingan.rpc.api.invoke.InvokeProxy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class RpcInvokeProxy implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public InvokeProxy findInvoke(String protocol) {
        InvokeProxy invokeProxy = applicationContext.getBean(protocol, InvokeProxy.class);
        AssertUtil.checkoutNotNull(invokeProxy, "cannot find InvokeProxy By " + protocol);
        return invokeProxy;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
