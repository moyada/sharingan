package io.moyada.sharingan.expression.test.repository;

import io.moyada.sharingan.domain.metadada.*;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MetadataRepositoryTest implements MetadataRepository {

    @Override
    public List<AppData> findAllApp() {
        return null;
    }

    @Override
    public AppData findAppById(int appId) {
        return null;
    }

    @Override
    public AppData findAppByName(String appName) {
        AppData appData = new AppData();
        appData.setId(1);
        appData.setName(appName);
        return appData;
    }

    @Override
    public List<AppData> findAppByIds(int[] appIds) {
        return null;
    }

    @Override
    public List<ServiceData> findServiceByApp(int appId) {
        return null;
    }

    @Override
    public ServiceData findServiceById(int serviceId) {
        return null;
    }

    @Override
    public List<MethodData> findMethodByService(int serviceId) {
        return null;
    }

    @Override
    public MethodData findMethodById(int funId) {
        return null;
    }

    @Override
    public List<HttpData> findHttpByService(int serviceId) {
        return null;
    }

    @Override
    public HttpData findHttpById(int methodId) {
        return null;
    }
}
