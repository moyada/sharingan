package cn.moyada.dubbo.faker.core;

import cn.moyada.dubbo.faker.core.exception.InitializeInvokerException;
import cn.moyada.dubbo.faker.core.invoke.AbstractInvoker;
import cn.moyada.dubbo.faker.core.invoke.AsyncInvoker;
import cn.moyada.dubbo.faker.core.listener.BatchLoggingListener;
import cn.moyada.dubbo.faker.core.listener.AbstractListener;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.MethodInvokeDO;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.provider.ParamProvider;
import cn.moyada.dubbo.faker.core.proxy.MethodHandleProxy;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.LockSupport;

@Component
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Autowired
    private FakerManager fakerManager;

    @Autowired
    private MethodHandleProxy methodHandleProxy;

    public String invoke(int invokeId, String invokeExpression, int poolSize,
                         int qps, long questNum, boolean random,
                         boolean saveResult, String resultParam) {

        MethodInvokeDO invokeInfo = fakerManager.getInvokeInfo(invokeId);
        MethodProxy proxy = methodHandleProxy.getProxy(invokeInfo); //, poolSize);

        Object[] values = JsonUtil.toArray(invokeExpression, Object[].class);
        Class<?>[] paramTypes = proxy.getParamTypes();
        if(null == values || paramTypes.length != values.length) {
            throw InitializeInvokerException.paramError;
        }

        // 生成调用报告序号
        String fakerId = UUIDUtil.getUUID();

        // 参数提供器
        ParamProvider paramProvider = new ParamProvider(fakerManager, values, paramTypes, random);

        // 创建调用结果监听器
        AbstractListener listener = new BatchLoggingListener(fakerId, invokeId, questNum,
                fakerManager, saveResult, resultParam);

        // 创建方法调用器
        AbstractInvoker invoke = new AsyncInvoker(proxy.getMethodHandle(), proxy.getService(),
                listener, poolSize);

        int timeout = (3600 / qps) - (20 >= qps ? 0 : 50);
        // 发起调用
        if(timeout > 50) {
            log.info("start timeout faker invoke: " + fakerId);
            for (int index = 0; index < questNum; index++) {
                invoke.invoke(paramProvider.fetchNextParam());
                LockSupport.parkNanos(timeout * 1_000L);
            }
        }
        else {
            log.info("start faker invoke: " + fakerId);
            for (int index = 0; index < questNum; index++) {
                invoke.invoke(paramProvider.fetchNextParam());
            }
        }

        invoke.shutdownDelay();

        log.info("faker invoke done: " + fakerId);

        listener.shutdownDelay();

        log.info("logging shutdown: " + fakerId);

        return "请求结果序号：" + fakerId;
    }
}
