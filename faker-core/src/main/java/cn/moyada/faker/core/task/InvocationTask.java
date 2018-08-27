package cn.moyada.faker.core.task;

import cn.moyada.faker.common.exception.InitializeInvokerException;
import cn.moyada.faker.common.utils.JsonUtil;
import cn.moyada.faker.common.utils.UUIDUtil;
import cn.moyada.faker.core.QuestInfo;
import cn.moyada.faker.core.convert.AppInfoConverter;
import cn.moyada.faker.manager.FakerManager;
import cn.moyada.faker.manager.domain.AppInfoDO;
import cn.moyada.faker.manager.domain.MethodInvokeDO;
import cn.moyada.faker.module.Dependency;
import cn.moyada.faker.module.fetch.MetadataFetch;
import cn.moyada.faker.module.InvokeInfo;
import cn.moyada.faker.module.InvokeMetadata;
import cn.moyada.faker.module.handler.MetadataWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvocationTask implements TaskActivity {
    private static final Logger log = LoggerFactory.getLogger(InvocationTask.class);

    @Autowired
    private FakerManager fakerManager;

    @Autowired
    private MetadataWrapper metadataWrapper;

    @Autowired
    private MetadataFetch metadataFetch;

    @Override
    public String runTask(QuestInfo questInfo) throws InitializeInvokerException {
        TaskEnvironment environment = generateEnv(questInfo);

        metadataFetch.checkoutClassLoader(environment.getDependency());

        // 生成调用报告序号
        String fakerId = UUIDUtil.getUUID();
        proxy.setFakerId(fakerId);
        proxy.setValues(values);

        InvokeTask invokeTask = new InvokeTask(proxy, questInfo);

        int qps = questInfo.getQps();
        int timeout = (3600 / qps) - (20 >= qps ? 0 : 50);
        // 发起调用
        if (timeout > 50) {
            invokeTask.start(timeout);
        } else {
            invokeTask.start();
        }

        invokeTask.shutdown();

        return "请求结果序号：" + fakerId;
    }

    private TaskEnvironment generateEnv(QuestInfo questInfo) throws InitializeInvokerException {
        MethodInvokeDO methodInvokeDO = fakerManager.getInvokeInfo(questInfo.getInvokeId());

        Dependency dependency = getDependency(methodInvokeDO.getAppId());

        InvokeInfo invokeInfo = getInvokeInfo(methodInvokeDO);

        InvokeMetadata invokeMetadata = metadataWrapper.getProxy(dependency, invokeInfo);

        Object[] values = JsonUtil.toArray(questInfo.getInvokeExpression(), Object[].class);
        Class<?>[] paramTypes = invokeMetadata.getParamTypes();
        if(null == values || paramTypes.length != values.length) {
            throw new IllegalStateException("param number error.");
        }

        TaskEnvironment env = new TaskEnvironment();
        env.setQuestInfo(questInfo);
        env.setDependency(dependency);
        env.setInvokeMetadata(invokeMetadata);
        return env;
    }

    private Dependency getDependency(int appId) {
        AppInfoDO appInfoDO = fakerManager.getDependencyByAppId(appId);
        if(null == appInfoDO) {
            throw new InitializeInvokerException("获取应用信息失败: " + appId);
        }

        Dependency dependency = AppInfoConverter.toDependency(appInfoDO);
        String dependencies = appInfoDO.getDependencies();
        List<AppInfoDO> appList = fakerManager.getDependencyByAppId(dependencies);
        dependency.setDependencyList(AppInfoConverter.toDependency(appList));
        return dependency;
    }

    private InvokeInfo getInvokeInfo(MethodInvokeDO methodInvokeDO) {
        InvokeInfo invokeInfo = new InvokeInfo();
        invokeInfo.setClassType(methodInvokeDO.getClassName());
        invokeInfo.setMethodName(methodInvokeDO.getMethodName());
        invokeInfo.setParamType(methodInvokeDO.getParamType());
        invokeInfo.setReturnType(methodInvokeDO.getReturnType());
        return invokeInfo;
    }
}
