package io.moyada.sharingan.expression.test;

import io.moyada.sharingan.domain.expression.DataRepository;
import io.moyada.sharingan.domain.metadada.MetadataRepository;
import io.moyada.sharingan.expression.provider.ArgsProvider;
import io.moyada.sharingan.expression.provider.ResourceReplaceProvider;
import io.moyada.sharingan.expression.test.repository.DataRepositoryTest;
import io.moyada.sharingan.expression.test.repository.MetadataRepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ResourceReplaceProviderTest {

    private MetadataRepository metadataRepository;
    private DataRepository dataRepository;

    @BeforeAll
    public void setRepository() {
        metadataRepository = new MetadataRepositoryTest();
        dataRepository = new DataRepositoryTest();
    }

    @Test
    public void randomResourceReplaceProviderTest() {
        ArgsProvider argsProvider = new ResourceReplaceProvider("10.#{target}", String.class, "#{target}",
                true, metadataRepository, dataRepository, "test", "test");
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
                false, metadataRepository, dataRepository, "test", "test");
        String prep = "", next;
        for (int i = 0; i < 1000; i++) {
            next = (String) argsProvider.fetchNext();
            Assertions.assertNotEquals(prep, next);
            prep = next;
        }
    }
}
