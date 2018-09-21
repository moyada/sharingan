package cn.moyada.sharingan.core.factory;

import cn.moyada.sharingan.common.exception.InitializeInvokerException;
import cn.moyada.sharingan.common.utils.AssertUtil;
import cn.moyada.sharingan.common.utils.JsonUtil;
import cn.moyada.sharingan.core.common.InvokeContext;
import cn.moyada.sharingan.core.common.QuestInfo;
import cn.moyada.sharingan.core.convert.AppInfoConverter;
import cn.moyada.sharingan.core.convert.FunctionInfoConverter;
import cn.moyada.sharingan.module.Dependency;
import cn.moyada.sharingan.module.InvokeInfo;
import cn.moyada.sharingan.module.InvokeMetaData;
import cn.moyada.sharingan.module.handler.InvokeAdapter;
import cn.moyada.sharingan.storage.api.MetadataRepository;
import cn.moyada.sharingan.storage.api.domain.AppDO;
import cn.moyada.sharingan.storage.api.domain.FunctionDO;
import cn.moyada.sharingan.storage.api.domain.ServiceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnvironmentFactory {

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private InvokeAdapter invokeAdapter;

    @SuppressWarnings("ConstantConditions")
    public InvokeContext buildEnv(QuestInfo questInfo) {
        Integer funcId = questInfo.getFuncId();
        FunctionDO functionDO = metadataRepository.findFunctionById(funcId);
        AssertUtil.checkoutNotNull(functionDO, "cannot find function by funcId: " + funcId);

        Integer serviceId = functionDO.getServiceId();
        ServiceDO serviceDO = metadataRepository.findServiceById(serviceId);
        AssertUtil.checkoutNotNull(serviceDO, "cannot find service by serviceId: " + serviceId);

        Dependency dependency = getDependency(functionDO.getAppId());
        InvokeInfo invokeInfo = FunctionInfoConverter.getInvokeInfo(functionDO);

        InvokeMetaData invokeMetaData = invokeAdapter.wrapper(dependency, invokeInfo);

        String[] expression = JsonUtil.toArray(questInfo.getExpression(), String[].class);
        Class<?>[] paramTypes = invokeMetaData.getParamTypes();
        if(null == expression && null != paramTypes) {
            throw new InitializeInvokerException("input expression param number cannot match with function param.");
        }
        else if(paramTypes.length != expression.length) {
            throw new InitializeInvokerException("input expression param number cannot match with function param.");
        }

        InvokeContext env = new InvokeContext();
        env.setAppId(serviceDO.getAppId());
        env.setServiceId(serviceId);
        env.setFuncId(funcId);

        env.setExpression(expression);
        env.setProtocol(serviceDO.getProtocol());
        env.setDependency(dependency);
        env.setInvokeMetaData(invokeMetaData);
        return env;
    }

    private Dependency getDependency(int appId) {
        AppDO appDO = metadataRepository.findAppById(appId);
        AssertUtil.checkoutNotNull(appDO, "获取应用信息失败: " + appId);

        Dependency dependency = AppInfoConverter.toDependency(appDO);

        String dependencies = appDO.getDependencies();
        List<AppDO> appList = metadataRepository.findApp(dependencies);
        dependency.setDependencyList(AppInfoConverter.toDependency(appList));
        return dependency;
    }
}
