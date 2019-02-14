package io.moyada.sharingan.spring.boot.autoconfigure.test;

import io.moyada.sharingan.spring.boot.autoconfigure.MonitorBeanDefinitionScanner;
import io.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import io.moyada.sharingan.spring.boot.autoconfigure.support.ListenerAnalyser;
import io.moyada.sharingan.spring.boot.autoconfigure.support.ListenerInfo;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Main {

    public static void main(String[] args) {
        Map<String, String> attach = new HashMap<>();
        attach.put("name", "sharingan");
        attach.put("id", "13245");

        MonitorBeanDefinitionScanner beanDefinitionScanner =
                new MonitorBeanDefinitionScanner(new DefaultListableBeanFactory(), new SharinganConfig(), null);

        Class<TestProxyClass> target = TestProxyClass.class;

        ListenerInfo listenerInfo = ListenerAnalyser.getListenerInfo(target);
        if (null != listenerInfo) {
//            beanDefinitionScanner.proxy(target, listenerInfo);
        }
    }
}
