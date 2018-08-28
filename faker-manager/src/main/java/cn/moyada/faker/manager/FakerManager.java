package cn.moyada.faker.manager;

import cn.moyada.faker.common.exception.InitializeInvokerException;
import cn.moyada.faker.common.utils.ConvertUtil;
import cn.moyada.faker.common.utils.StringUtil;
import cn.moyada.faker.manager.dao.FakerDAO;
import cn.moyada.faker.manager.domain.AppInfoDO;
import cn.moyada.faker.manager.domain.LogDO;
import cn.moyada.faker.manager.domain.MethodInvokeDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author xueyikang
 * @create 2017-12-30 05:57
 */
@Repository
public class FakerManager {

    private static final Logger log = LoggerFactory.getLogger(FakerManager.class);

    @Autowired
    private FakerDAO fakerDAO;

    public List<MethodInvokeDO> getAll() {
        return fakerDAO.findAll();
    }

    public List<AppInfoDO> getAllApp() {
        return fakerDAO.findAllApp();
    }

    public void updateUrl(String groupId, String artifactId, String url) {
        fakerDAO.updateUrl(groupId, artifactId, url);
    }

    public AppInfoDO getDependencyByAppId(int id) {
        return fakerDAO.findAppById(id);
    }

    public List<AppInfoDO> getDependencyByAppId(String dependencies) {
        if (StringUtil.isEmpty(dependencies)) {
            return null;
        }

        String[] split = dependencies.split(",");
        if (split.length == 0) {
            return null;
        }
        int[] ids = ConvertUtil.convertInt(split);
        return fakerDAO.findDependencyById(ids);
    }

    public List<String> getClassByApp(int appId) {
        return fakerDAO.findClassByApp(appId);
    }

    public List<MethodInvokeDO> getMethodByClass(String className) {
        return fakerDAO.findMethodByClass(className);
    }

    public int countMethodByFakerId(String fakerId) {
        return fakerDAO.countMethodByFakerId(fakerId);
    }

    public List<LogDO> getMethodByFakerId(String fakerId, int pageIndex, int pageSize) {
        int offset = pageSize * (pageIndex - 1);
        return fakerDAO.findMethodByFakerId(fakerId, offset, pageSize);
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
        MethodInvokeDO methodInvoke = fakerDAO.findInvokeInfoById(id);
        if(null == methodInvoke) {
            log.error("unknow invokeId: " + id);
            throw new InitializeInvokerException("unknow invokeId: " + id);
        }
        return methodInvoke;
    }

    public List<String> getFakerParamByRebuildParam(String param) {
        String[] split = param.split("\\.");
        Integer appId = Integer.valueOf(split[0]);
        String type = split[1];
        int count = fakerDAO.countParamByType(appId, type);
        int limit, page;
        if(count < 1000) {
            limit = 0;
            page = count;
        }
        else {
            limit = new Random().nextInt(count - 1000);
            page = 1000;
        }

        return fakerDAO.findParamByType(appId, type, limit, page);
    }

    /**
     * 根据参数表达式获取数据库预存模拟参数
     * @param paramSet 表达式集合
     * @return
     */
    public Map<String, List<String>> getFakerParamMapByRebuildParam(Set<String> paramSet) {
        if(null == paramSet || paramSet.isEmpty()) {
            return null;
        }
        Map<String, List<String>> paramMap = new HashMap<>(paramSet.size());
        List<String> paramValueList;
        for (String param : paramSet) {
            //TODO 过滤从结果中再获取参数的
//            if('_' == param.indexOf(0)) {
//                continue;
//            }
            paramValueList = this.getFakerParamByRebuildParam(param);
            if(paramValueList.isEmpty()) {
                throw new InitializeInvokerException("获取 " + param + " 实际参数集合失败，请检查数据库.");
            }

            paramMap.put(param, paramValueList);
        }
        return paramMap;
    }

    /**
     * 保存调用信息
     * @param logDO
     */
    public void saveLog(LogDO logDO) {
        fakerDAO.saveLog(logDO);
    }

    public void saveLog(List<LogDO> logDO) {
        fakerDAO.saveLogList(logDO);
    }
}
