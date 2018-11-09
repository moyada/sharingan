package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.*;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ClassUtilTest {

    public static void main(String[] args) throws NoSuchMethodException, NotFoundException, ClassNotFoundException, InvocationTargetException, CannotCompileException, IllegalAccessException, IOException, InstantiationException {
        Interface2 testProxyClass;

        List<ProxyMethod> proxyInfo = ClassUtil.getProxyInfo(TestProxyClass.class, Exclusive.class);
        System.out.println(proxyInfo);

        JavassistProxy<Invocation> javassistProxy = new JavassistProxy<>(Monitor.class,
                Monitor.class.getDeclaredMethod("listener", Invocation.class),
                Invocation.class, DefaultInvocation.class, new String[]{"application"}
        );

        Class<TestProxyClass> wrapper = javassistProxy.wrapper(TestProxyClass.class, proxyInfo);

        testProxyClass = wrapper.newInstance();
        System.out.println(wrapper);
        testProxyClass.go(123);
    }
}
