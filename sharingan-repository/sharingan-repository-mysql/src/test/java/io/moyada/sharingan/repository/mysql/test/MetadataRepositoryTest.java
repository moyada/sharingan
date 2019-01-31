package io.moyada.sharingan.repository.mysql.test;


import io.moyada.sharingan.domain.metadada.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MetadataRepositoryTest {

    @Autowired
    private MetadataRepository metadataRepository;

    @Test
    public void findAllAppTest() {
        List<AppData> apps = metadataRepository.findAllApp();
        Assertions.assertNotNull(apps);
    }

    @Test
    public void findAppByIdTest() {
        AppData appData = metadataRepository.findAppById(1);
        Assertions.assertNotNull(appData);
    }

    @Test
    public void findAppByNameTest() {
        AppData appData = metadataRepository.findAppByName("dubbo-test");
        Assertions.assertNotNull(appData);
    }

    @Test
    public void findAppByIdsTest() {
        List<AppData> apps = metadataRepository.findAppByIds(new int[]{1});
        Assertions.assertNotNull(apps);
    }

    @Test
    public void findServiceByAppTest() {
        List<ServiceData> service = metadataRepository.findServiceByApp(1);
        Assertions.assertNotNull(service);
    }

    @Test
    public void findServiceByIdTest() {
        ServiceData service = metadataRepository.findServiceById(1);
        Assertions.assertNotNull(service);
    }

    @Test
    public void findMethodByServiceTest() {
        List<MethodData> methodData = metadataRepository.findMethodByService(1);
        Assertions.assertNotNull(methodData);
    }

    @Test
    public void findMethodByIdTest() {
        MethodData methodData = metadataRepository.findMethodById(1);
        Assertions.assertNotNull(methodData);
    }

    @Test
    public void findHttpByServiceTest() {
        List<HttpData> httpData = metadataRepository.findHttpByService(1);
        Assertions.assertNotNull(httpData);
    }

    @Test
    public void findHttpByIdTest() {
        HttpData httpData = metadataRepository.findHttpById(1);
        Assertions.assertNotNull(httpData);
    }
}
