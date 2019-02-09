package io.moyada.sharingan.expression.test;

import io.moyada.sharingan.expression.provider.ArgsProvider;
import io.moyada.sharingan.expression.provider.NumberProvider;
import io.moyada.sharingan.expression.range.NumberRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class NumberProviderTest {

    @Test
    public void basicTest() {
        NumberRange numberRange = new NumberRange(-10, 80);
        ArgsProvider argsProvider = new NumberProvider("#{int.[-10-80]}", int.class, "#{int.[-10-80]}", numberRange);
        int value;
        for (int i = 0; i < 1000; i++) {
            value = (int) argsProvider.fetchNext();
            Assertions.assertTrue(-10 <= value && value <= 80);
        }
    }

    @Test
    public void combineTest() {
        NumberRange numberRange = new NumberRange(0, 10);
        ArgsProvider argsProvider = new NumberProvider("d-#{int.[0-10]}", String.class, "#{int.[0-10]}", numberRange);
        String value;
        for (int i = 0; i < 1000; i++) {
            value = (String) argsProvider.fetchNext();
            Integer integer = Integer.valueOf(value.substring(2));
            Assertions.assertTrue(0 <= integer && integer <= 10);
        }
    }
}
