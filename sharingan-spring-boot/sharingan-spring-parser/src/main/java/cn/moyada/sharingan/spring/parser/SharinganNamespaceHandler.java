package cn.moyada.sharingan.spring.parser;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class SharinganNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("application", new MonitorBeanDefinitionParser());
    }
}
