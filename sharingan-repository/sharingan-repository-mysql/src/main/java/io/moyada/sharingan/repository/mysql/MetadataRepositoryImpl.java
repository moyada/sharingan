package io.moyada.sharingan.repository.mysql;



import io.moyada.sharingan.domain.metadada.*;
import io.moyada.sharingan.repository.mysql.dao.AppDAO;
import io.moyada.sharingan.repository.mysql.dao.HttpDAO;
import io.moyada.sharingan.repository.mysql.dao.MethodDAO;
import io.moyada.sharingan.repository.mysql.dao.ServiceDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MetadataRepositoryImpl implements MetadataRepository {

    @Autowired
    private AppDAO appDAO;

    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    private MethodDAO methodDAO;

    @Autowired
    private HttpDAO httpDAO;

    @Override
    public List<AppData> findAllApp() {
        return appDAO.findAll();
    }

    @Override
    public AppData findAppById(int appId) {
        return appDAO.findById(appId);
    }

    @Override
    public AppData findAppByName(String appName) {
        return appDAO.findByName(appName);
    }

    @Override
    public List<AppData> findAppByIds(int[] appIds) {
        return appDAO.findByIds(appIds);
    }

    @Override
    public List<ServiceData> findServiceByApp(int appId) {
        return serviceDAO.findByApp(appId);
    }

    @Override
    public ServiceData findServiceById(int serviceId) {
        return serviceDAO.findById(serviceId);
    }

    @Override
    public List<MethodData> findMethodByService(int serviceId) {
        return methodDAO.findByService(serviceId);
    }

    @Override
    public MethodData findMethodById(int funId) {
        return methodDAO.findById(funId);
    }

    @Override
    public List<HttpData> findHttpByService(int serviceId) {
        return httpDAO.findByService(serviceId);
    }

    @Override
    public HttpData findHttpById(int methodId) {
        return httpDAO.findById(methodId);
    }
}
