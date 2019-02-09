package io.moyada.sharingan.infrastructure.test;

import io.moyada.sharingan.infrastructure.module.Dependency;
import io.moyada.sharingan.infrastructure.module.MetadataFetch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ModuleTest {

    @Autowired
    private MetadataFetch metadataFetch;

    @Test
    public void getClassLoaderTest() {
        Dependency dependency = new Dependency("cn.moyada", "dubbo-test-api", null, null);
        ClassLoader classLoader = metadataFetch.getClassLoader(dependency);
        Assertions.assertNotNull(classLoader);
    }

    @Test
    public void getClassTest() {
        Dependency dependency = new Dependency("cn.moyada", "dubbo-api", null, null);
        Assertions.assertDoesNotThrow(() -> metadataFetch.getClass(dependency, "cn.moayda.rpc.demo.dubbo.api.UserService"));
    }
}
