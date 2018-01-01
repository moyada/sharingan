package cn.xueyikang.dubbo.faker.core.manager;

import cn.xueyikang.dubbo.faker.core.dao.FakerDAO;
import cn.xueyikang.dubbo.faker.core.exception.NoSuchParamException;
import cn.xueyikang.dubbo.faker.core.model.LogDO;
import cn.xueyikang.dubbo.faker.core.model.MethodInvokeDO;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author xueyikang
 * @create 2017-12-30 05:57
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class FakerManager {

    private static final Logger log = LoggerFactory.getLogger(FakerManager.class);

    @Autowired
    private FakerDAO fakerDAO;

    public List<MethodInvokeDO> getAll() {
        return fakerDAO.findAll();
    }

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
        String[] split = param.substring(2, param.length() - 1).split("\\.");
        Integer appId = Integer.valueOf(split[0]);
        String type = split[1];
        return fakerDAO.findParamByType(appId, type);
    }
    public Map<String, List<String>> getFakerParamMapByRebuildParam(Set<String> paramSet) {
        if(null == paramSet || paramSet.isEmpty()) {
            return null;
        }
        Map<String, List<String>> paramMap = Maps.newHashMapWithExpectedSize(paramSet.size());
        List<String> paramValueList;
        for (String param : paramSet) {
            paramValueList = this.getFakerParamByRebuildParam(param);
            if(paramValueList.isEmpty()) {
                throw new NoSuchParamException(param + " don't have any param, please checkout invoke_param table.");
            }
            else {
                paramMap.put(param, paramValueList);
            }
        }
        return paramMap;
    }

    public void saveLog(LogDO logDO) {
        fakerDAO.saveLog(logDO);
    }

    public static void main(String[] args) {
        String param = "${1.model}";
        String substring = param.substring(2, param.length() - 1);
        String[] split = substring.split("\\.");
        Integer appId = Integer.valueOf(split[0]);
        String type = split[1];
    }
}
