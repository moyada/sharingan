package io.moyada.sharingan.expression.test;

import io.moyada.sharingan.domain.expression.ProviderFactory;
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
public class ExpressionProviderTest {

    @Autowired
    private ProviderFactory providerFactory;

    @Test
    public void basicTest() {

    }
}
