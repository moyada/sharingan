package cn.moyada.sharingan.agent.spring;

import cn.moyada.sharingan.agent.spring.parser.MonitorBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class SharinganNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("monitor", new MonitorBeanDefinitionParser());
    }
}
