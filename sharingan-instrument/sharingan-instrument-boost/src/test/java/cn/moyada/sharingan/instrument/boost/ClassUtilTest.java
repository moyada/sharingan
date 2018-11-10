package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.*;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ClassUtilTest {

    public static void main(String[] args) throws NoSuchMethodException, NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        AbstractClass testProxyClass;

        List<ProxyMethod> proxyInfo = ClassUtil.getProxyInfo(TestProxyClass.class, Exclusive.class);
        System.out.println(proxyInfo);

        JavassistProxy<Invocation> javassistProxy = new JavassistProxy<>(Monitor.class,
                Monitor.class.getDeclaredMethod("listener", Invocation.class),
                Invocation.class, DefaultInvocation.class, new String[]{"application"}
        );

        Class<TestProxyClass> wrapper = javassistProxy.wrapper(TestProxyClass.class, proxyInfo);

        testProxyClass = wrapper.newInstance();

        Field monitor = wrapper.getDeclaredField("_monitor");
        monitor.setAccessible(true);
        monitor.set(testProxyClass, new TestMonitor());

        Field application = wrapper.getDeclaredField("_application");
        application.setAccessible(true);
        application.set(testProxyClass, "ceshi");

        System.out.println(wrapper);
        testProxyClass.jiade("123");
    }
}
