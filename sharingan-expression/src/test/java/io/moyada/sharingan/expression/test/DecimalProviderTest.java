package io.moyada.sharingan.expression.test;

import io.moyada.sharingan.expression.provider.ArgsProvider;
import io.moyada.sharingan.expression.provider.DecimalProvider;
import io.moyada.sharingan.expression.range.DecimalRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DecimalProviderTest {

    @Test
    public void basicTest() {
        DecimalRange decimalRange = new DecimalRange(-20.5, 90.05, 3);
        ArgsProvider argsProvider = new DecimalProvider("#{double.[-20.5-90.05]}", double.class, "#{double.[-20.5-90.05]}", decimalRange);
        double value;
        for (int i = 0; i < 1000; i++) {
            value = (double) argsProvider.fetchNext();
            Assertions.assertTrue(-20.5 <= value && value <= 90.05);
        }
    }

    @Test
    public void precisionTest() {
        DecimalRange decimalRange = new DecimalRange(0, 100, 4);
        ArgsProvider argsProvider = new DecimalProvider("#{double[4].[0-100]}", double.class, "#{double[4].[0-100]}", decimalRange);
        double value;
        for (int i = 0; i < 1000; i++) {
            value = (double) argsProvider.fetchNext();
            String str = String.valueOf(value);
            int index = str.indexOf(".") + 1;
            Assertions.assertTrue(str.length() - index <= 4);
        }
    }

    @Test
    public void combineTest() {
        DecimalRange decimalRange = new DecimalRange(0, 100, 3);
        ArgsProvider argsProvider = new DecimalProvider("d-#{double.[0-100]}", String.class, "#{double.[0-100]}", decimalRange);
        String value;
        for (int i = 0; i < 1000; i++) {
            value = (String) argsProvider.fetchNext();
            Double aDouble = Double.valueOf(value.substring(2));
            Assertions.assertTrue(0 <= aDouble && aDouble <= 100);
        }
    }
}
