package io.moyada.sharingan.expression.test;

import io.moyada.sharingan.expression.provider.ArgsProvider;
import io.moyada.sharingan.expression.provider.ConstantProvider;
import io.moyada.sharingan.expression.provider.ConstantReplaceProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ConstantProviderTest {

    @Test
    public void ConstantProviderTest() {
        ArgsProvider argsProvider = new ConstantProvider("30", int.class);
        Assertions.assertEquals(30, (int) argsProvider.fetchNext());
    }

    @Test
    public void ConstantReplaceProviderTest() {
        ArgsProvider argsProvider = new ConstantReplaceProvider("10.#{target}", "#{target}", 35, double.class);
        System.out.println(argsProvider.fetchNext());
    }
}
