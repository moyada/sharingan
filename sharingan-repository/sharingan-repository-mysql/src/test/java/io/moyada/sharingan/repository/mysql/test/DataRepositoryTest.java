package io.moyada.sharingan.repository.mysql.test;


import io.moyada.sharingan.domain.expression.DataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DataRepositoryTest {

    @Autowired
    private DataRepository dataRepository;

    @Test
    public void countTest() {
        int count = dataRepository.count(1, "model");
        Assertions.assertTrue(count > 0);
    }

    @Test
    public void findRandomArgsTest() {
        List<String> args = dataRepository.findRandomArgs(1, "model", 1000, 100);
        Assertions.assertNotNull(args);
    }
}
