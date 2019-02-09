package io.moyada.sharingan.expression.test;

import io.moyada.sharingan.domain.expression.DataRepository;
import io.moyada.sharingan.expression.RouteInfo;
import io.moyada.sharingan.expression.provider.ArgsProvider;
import io.moyada.sharingan.expression.provider.ResourceReplaceProvider;
import io.moyada.sharingan.infrastructure.util.UUIDUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ResourceReplaceProviderTest {

    @Test
    public void randomResourceReplaceProviderTest() {
        ArgsProvider argsProvider = new ResourceReplaceProvider("10.#{target}", String.class, "#{target}",
                true, new DataRepositoryTest(), new RouteInfo(1, "test"));
        String prep = "", next;
        for (int i = 0; i < 1000; i++) {
            next = (String) argsProvider.fetchNext();
            Assertions.assertNotEquals(prep, next);
            prep = next;
        }
    }

    @Test
    public void sequenceResourceReplaceProviderTest() {
        ArgsProvider argsProvider = new ResourceReplaceProvider("t-#{target}", String.class, "#{target}",
                false, new DataRepositoryTest(), new RouteInfo(1, "test"));
        String prep = "", next;
        for (int i = 0; i < 1000; i++) {
            next = (String) argsProvider.fetchNext();
            Assertions.assertNotEquals(prep, next);
            prep = next;
        }
    }

    class DataRepositoryTest implements DataRepository {

        @Override
        public int count(int appId, String domain) {
            return 200;
        }

        @Override
        public List<String> findRandomArgs(int appId, String domain, int total, int size) {
            List<String> data = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                data.add(UUIDUtil.getUUID() + i);
            }
            return data;
        }
    }
}
