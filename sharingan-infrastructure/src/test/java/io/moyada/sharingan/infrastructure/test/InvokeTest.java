package io.moyada.sharingan.infrastructure.test;

import io.moyada.sharingan.infrastructure.invoke.DefaultMethodInvoke;
import org.junit.jupiter.api.BeforeAll;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class InvokeTest {

    private DefaultMethodInvoke asyncMethodInvoke;

    @BeforeAll
    public void setup() {
        asyncMethodInvoke = new DefaultMethodInvoke();
    }
}
