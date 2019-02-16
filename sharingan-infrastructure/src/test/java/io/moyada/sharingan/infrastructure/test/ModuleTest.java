package io.moyada.sharingan.infrastructure.test;

import io.moyada.sharingan.infrastructure.config.MavenConfig;
import io.moyada.sharingan.infrastructure.module.ArtifactFetch;
import io.moyada.sharingan.infrastructure.module.Dependency;
import io.moyada.sharingan.infrastructure.module.MetadataFetch;
import io.moyada.sharingan.infrastructure.module.Nexus3RestFetch;
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
    public void jarTest() {
        MavenConfig mavenConfig = new MavenConfig();
        mavenConfig.setRegistry("http://127.0.0.1:8081");

        ArtifactFetch dependencyFetch = new Nexus3RestFetch(mavenConfig);

        Dependency dependency = new Dependency("io.moyada", "dubbo-test-api");
        String url = dependencyFetch.getJarUrl(dependency);

        Assertions.assertNotNull(url);
    }

    @Test
    public void getClassLoaderTest() {
        Dependency dependency = new Dependency("io.moyada", "dubbo-test-api", null, null);
        ClassLoader classLoader = metadataFetch.getClassLoader(dependency);
        Assertions.assertNotNull(classLoader);
    }

    @Test
    public void getClassTest() {
        Dependency dependency = new Dependency("io.moyada", "dubbo-api", null, null);
        Assertions.assertDoesNotThrow(() -> metadataFetch.getClass(dependency, "io.moayda.rpc.demo.dubbo.api.UserService"));
    }
}
