package cn.moyada.sharingan.storage.mysql;

import cn.moyada.sharingan.common.utils.ConvertUtil;
import cn.moyada.sharingan.common.utils.StringUtil;
import cn.moyada.sharingan.storage.api.MetadataRepository;
import cn.moyada.sharingan.storage.api.domain.AppDO;
import cn.moyada.sharingan.storage.api.domain.FunctionDO;
import cn.moyada.sharingan.storage.api.domain.ServiceDO;
import cn.moyada.sharingan.storage.mysql.dao.AppDAO;
import cn.moyada.sharingan.storage.mysql.dao.FunctionDAO;
import cn.moyada.sharingan.storage.mysql.dao.ServiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MetadataRepositoryImpl implements MetadataRepository {

    @Autowired
    private AppDAO appDAO;

    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    private FunctionDAO functionDAO;

    @Override
    public List<AppDO> findAllApp() {
        return appDAO.findAll();
    }

    @Override
    public AppDO findAppById(int appId) {
        return appDAO.findById(appId);
    }

    @Override
    public AppDO findAppByName(String appName) {
        return appDAO.findByName(appName);
    }

    @Override
    public List<AppDO> findApp(String dependencies) {
        if (StringUtil.isEmpty(dependencies)) {
            return null;
        }

        String[] split = dependencies.split(",");
        if (split.length == 0) {
            return null;
        }
        int[] ids = ConvertUtil.convertInt(split);
        return appDAO.findByIds(ids);
    }

    @Override
    public List<ServiceDO> findServiceByApp(int appId) {
        return serviceDAO.findByApp(appId);
    }

    @Override
    public ServiceDO findServiceById(int serviceId) {
        return serviceDAO.findById(serviceId);
    }

    @Override
    public List<FunctionDO> findFunctionByService(int serviceId) {
        return functionDAO.findByService(serviceId);
    }

    @Override
    public FunctionDO findFunctionById(int funId) {
        return functionDAO.findById(funId);
    }
}
