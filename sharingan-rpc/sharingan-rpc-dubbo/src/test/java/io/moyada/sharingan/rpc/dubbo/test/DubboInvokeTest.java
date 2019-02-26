package io.moyada.sharingan.rpc.dubbo.test;

import io.moyada.sharingan.infrastructure.config.DefaultConfig;
import io.moyada.sharingan.infrastructure.config.MavenConfig;
import io.moyada.sharingan.infrastructure.invoke.Invocation;
import io.moyada.sharingan.infrastructure.invoke.data.ClassInvocation;
import io.moyada.sharingan.infrastructure.invoke.data.Result;
import io.moyada.sharingan.infrastructure.module.*;
import io.moyada.sharingan.rpc.dubbo.DubboInvoke;
import io.moyada.sharingan.rpc.dubbo.config.DubboConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DubboInvokeTest {

    private static MetadataFetch metadataFetch;

    private static DubboInvoke dubboInvoke;

    @BeforeAll
    public static void setup() {
        DefaultConfig defaultConfig = new DefaultConfig();
        defaultConfig.setIdentifyName("test");

        DubboConfig dubboConfig = new DubboConfig();
        dubboConfig.setRegistry("zookeeper://127.0.0.1:2181");
        dubboInvoke = new DubboInvoke(defaultConfig, dubboConfig);
    }

    @BeforeAll
    public static void maven() {
        MavenConfig mavenConfig = new MavenConfig();
        mavenConfig.setRegistry("http://127.0.0.1:8081");

        ArtifactFetch dependencyFetch = new Nexus3RestFetch(mavenConfig);
        metadataFetch = new ModuleFetch(dependencyFetch);
    }

    @Test
    public void invoke() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
        Dependency dependency = new Dependency("io.moyada", "dubbo-api");
        ClassLoader classLoader = metadataFetch.getClassLoader(dependency);
        Thread.currentThread().setContextClassLoader(classLoader);

        Class clazz = metadataFetch.getClass(dependency, "io.moyada.rpc.demo.dubbo.api.UserService");

        MethodHandles.Lookup lookup = metadataFetch.getMethodLookup(dependency);

        // 创建方法信息
        MethodType methodType = MethodType.methodType(
                metadataFetch.getClass(dependency, "io.moyada.rpc.demo.dubbo.api.UserInfo"),
                metadataFetch.getClass(dependency, "java.lang.Integer"));
        // 查询方法返回方法具柄
        MethodHandle methodHandle = lookup.findVirtual(clazz, "getById", methodType);

        ClassInvocation classInvocation = new ClassInvocation("dubbo-test", "UserService", "getById",
                clazz, new Class<?>[]{Integer.class}, methodHandle);
        dubboInvoke.initialize(classInvocation);

        Invocation invocation = new Invocation(new Object[]{100});
        Result result = dubboInvoke.execute(invocation);
        System.out.println(result.getException());

        Assertions.assertTrue(result.isSuccess());
    }
}
