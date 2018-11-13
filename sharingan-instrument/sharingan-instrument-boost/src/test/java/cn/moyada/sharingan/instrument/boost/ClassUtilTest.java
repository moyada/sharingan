package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistInheritProxy;
import cn.moyada.sharingan.instrument.boost.proxy.JavassistProxy;
import cn.moyada.sharingan.monitor.api.Monitor;
import cn.moyada.sharingan.monitor.api.TestMonitor;
import cn.moyada.sharingan.monitor.api.annotation.Exclusive;
import cn.moyada.sharingan.monitor.api.entity.DefaultInvocation;
import cn.moyada.sharingan.monitor.api.entity.Invocation;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ClassUtilTest {

    public static void main(String[] args) throws NoSuchMethodException, NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        TestProxyClass testProxyClass;

        List<ProxyMethod> proxyInfo = ClassUtil.getProxyInfo(TestProxyClass.class, Exclusive.class);
        System.out.println(proxyInfo);

        JavassistProxy<Invocation> javassistProxy = new JavassistInheritProxy<>(Monitor.class,
                Monitor.class.getDeclaredMethod("listener", Invocation.class),
                Invocation.class, DefaultInvocation.class, null, "application"
        );

        Class<TestProxyClass> wrapper = javassistProxy.wrapper(TestProxyClass.class, proxyInfo);

        System.out.println(Arrays.toString(wrapper.getDeclaredFields()));

        testProxyClass = wrapper.newInstance();

        Field monitor = wrapper.getDeclaredField("_monitor_");
        monitor.setAccessible(true);
        monitor.set(testProxyClass, new TestMonitor());

        Field application = wrapper.getDeclaredField("_application_");
        application.setAccessible(true);
        application.set(testProxyClass, "ceshi");

        Field num = AbstractClass.class.getDeclaredField("num");
        num.setAccessible(true);
        num.set(testProxyClass, 2);

        System.out.println("-------");
        testProxyClass.go(123);
    }
}
