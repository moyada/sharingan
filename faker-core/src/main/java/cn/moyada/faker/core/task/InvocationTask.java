package cn.moyada.faker.core.task;

import cn.moyada.faker.common.exception.InitializeInvokerException;
import cn.moyada.faker.common.utils.JsonUtil;
import cn.moyada.faker.common.utils.UUIDUtil;
import cn.moyada.faker.core.common.QuestInfo;
import cn.moyada.faker.core.convert.AppInfoConverter;
import cn.moyada.faker.core.listener.AbstractListener;
import cn.moyada.faker.core.listener.BatchLoggingListener;
import cn.moyada.faker.core.queue.AbstractQueue;
import cn.moyada.faker.core.queue.ArrayQueue;
import cn.moyada.faker.core.queue.AtomicQueue;
import cn.moyada.faker.core.queue.UnlockQueue;
import cn.moyada.faker.manager.FakerManager;
import cn.moyada.faker.manager.domain.AppInfoDO;
import cn.moyada.faker.manager.domain.LogDO;
import cn.moyada.faker.manager.domain.MethodInvokeDO;
import cn.moyada.faker.module.Dependency;
import cn.moyada.faker.module.InvokeInfo;
import cn.moyada.faker.module.InvokeMetadata;
import cn.moyada.faker.module.fetch.MetadataFetch;
import cn.moyada.faker.module.handler.MetadataWrapper;
import cn.moyada.faker.rpc.api.invoke.InvocationMetaDate;
import cn.moyada.faker.rpc.api.invoke.Result;
import cn.moyada.faker.rpc.dubbo.invocation.DubboInvoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Scope("prototype")
@Component
public class InvocationTask implements TaskActivity {
    private static final Logger log = LoggerFactory.getLogger(InvocationTask.class);

    @Autowired
    private FakerManager fakerManager;

    @Autowired
    private MetadataWrapper metadataWrapper;

    @Autowired
    private MetadataFetch metadataFetch;

    @Autowired
    private DubboInvoke dubboInvoker;

    @Override
    public String runTask(QuestInfo questInfo) throws InitializeInvokerException {
        TaskEnvironment environment = generateEnv(questInfo);

        metadataFetch.checkoutClassLoader(environment.getDependency());

        // 生成调用报告序号
        environment.setFakerId(UUIDUtil.getUUID());

        final AbstractQueue<LogDO> queue = buildQueue(environment.getQuestInfo());

        AbstractListener listener = new BatchLoggingListener(environment, queue);

        InvokeTask invokeTask = new InvokeTask(proxy, questInfo);

        InvocationMetaDate invocationMetaDate = getMetaDate(environment.getInvokeMetadata());
        dubboInvoker.prepare(invocationMetaDate);

        int qps = questInfo.getQps();
        int timeout = (3600 / qps) - (20 >= qps ? 0 : 50);
        // 发起调用
        if (timeout > 50) {
            invokeTask.start(timeout);
        } else {
            invokeTask.start();
        }

        invokeTask.shutdown();

        metadataFetch.recover();

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

    private AbstractQueue<LogDO> buildQueue(QuestInfo questInfo) {
        final AbstractQueue<LogDO> queue;
        switch (questInfo.getPoolSize()) {
            case 1:
                queue = new ArrayQueue<>(questInfo.getQuestNum());
                break;
            case 2:
                queue = new AtomicQueue<>(questInfo.getQuestNum());
                break;
            default:
                queue = UnlockQueue.build(questInfo.getPoolSize(), questInfo.getQuestNum());
        }
        return queue;
    }

    private InvokeInfo getInvokeInfo(MethodInvokeDO methodInvokeDO) {
        InvokeInfo invokeInfo = new InvokeInfo();
        invokeInfo.setClassType(methodInvokeDO.getClassName());
        invokeInfo.setMethodName(methodInvokeDO.getMethodName());
        invokeInfo.setParamType(methodInvokeDO.getParamType());
        invokeInfo.setReturnType(methodInvokeDO.getReturnType());
        return invokeInfo;
    }

    private InvocationMetaDate getMetaDate(InvokeMetadata invokeMetadata) {
        InvocationMetaDate invocationMetaDate = new InvocationMetaDate();
        invocationMetaDate.setService(invokeMetadata.getClassType());
        invocationMetaDate.setMethodHandle(invokeMetadata.getMethodHandle());
        return invocationMetaDate;
    }
}
