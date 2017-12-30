package cn.xueyikang.dubbo.faker.core.manager;

import cn.xueyikang.dubbo.faker.core.dao.FakerDAO;
import cn.xueyikang.dubbo.faker.core.model.LogDO;
import cn.xueyikang.dubbo.faker.core.model.MethodInvokeDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xueyikang
 * @create 2017-12-30 05:57
 */
@Component
public class FakerManager {

    private static final Logger log = LoggerFactory.getLogger(FakerManager.class);

    @Autowired
    private FakerDAO fakerDAO;

    public boolean saveInfokeInfo(MethodInvokeDO methodInvokeDO) {
        try {
            fakerDAO.saveInvokeInfo(methodInvokeDO);
        }
        catch (Exception e) {
            log.error("save method invoke error" + e);
            return false;
        }
        return true;
    }

    public MethodInvokeDO getInvokeInfo(int id) {
        return fakerDAO.findInvokeInfoById(id);
    }

    public List<String> getFakerParam(int appId, String type) {
        return fakerDAO.findParamByType(appId, type);
    }

    public List<String> getFakerParamByRebuildParam(String param) {
        String[] split = param.substring(1, param.length() - 1).split(".");
        Integer appId = Integer.valueOf(split[0]);
        String type = split[1];
        return fakerDAO.findParamByType(appId, type);
    }

    public void saveLog(LogDO logDO) {
        fakerDAO.saveLog(logDO);
    }
}
