package cn.moyada.faker.core.task;

import cn.moyada.faker.common.exception.InitializeInvokerException;
import cn.moyada.faker.common.utils.JsonUtil;
import cn.moyada.faker.common.utils.UUIDUtil;
import cn.moyada.faker.core.common.QuestInfo;
import cn.moyada.faker.core.convert.AppInfoConverter;
import cn.moyada.faker.core.invoke.DefaultExecutor;
import cn.moyada.faker.core.invoke.JobAction;
import cn.moyada.faker.core.listener.BatchLoggingListener;
import cn.moyada.faker.core.listener.ListenerAction;
import cn.moyada.faker.core.provider.ParamProvider;
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
import cn.moyada.faker.rpc.dubbo.invocation.DubboInvoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvocationTask implements TaskActivity {
    private static final Logger log = LoggerFactory.getLogger(InvocationTask.class);

    @Autowired
    private FakerManager fakerManager;

    @Autowired
    private MetadataWrapper metadataWrapper;

    @Qualifier("moduleFetch")
    @Autowired
    private MetadataFetch metadataFetch;

    @Autowired
    private DubboInvoke dubboInvoker;

    @Override
    public String runTask(QuestInfo questInfo) throws InitializeInvokerException {
        TaskEnvironment environment = generateEnv(questInfo);
        Object[] values = JsonUtil.toArray(questInfo.getInvokeExpression(), Object[].class);

        metadataFetch.checkoutClassLoader(environment.getDependency());

        // 生成调用报告序号
        final String fakerId = UUIDUtil.getUUID();
        environment.setFakerId(fakerId);

        final AbstractQueue<LogDO> queue = buildQueue(environment.getQuestInfo());

        final ListenerAction listener = new BatchLoggingListener(environment, queue);

        ParamProvider paramProvider = new ParamProvider(values, environment.getInvokeMetadata().getParamTypes(), questInfo.isRandom());

        final JobAction action = new DefaultExecutor(fakerId, questInfo);

        final InvocationMetaDate invocationMetaDate = getMetaDate(environment.getInvokeMetadata());

        dubboInvoker.prepare(invocationMetaDate);

        AbstractTaskActivity taskActivity = new AbstractTaskActivity(dubboInvoker, listener, paramProvider, action);

        taskActivity.start(questInfo);

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
