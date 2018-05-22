package cn.moyada.dubbo.faker.core;

import cn.moyada.dubbo.faker.core.exception.InitializeInvokerException;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.InvokerInfo;
import cn.moyada.dubbo.faker.core.model.MethodProxy;
import cn.moyada.dubbo.faker.core.model.domain.MethodInvokeDO;
import cn.moyada.dubbo.faker.core.proxy.MethodHandleProxy;
import cn.moyada.dubbo.faker.core.task.InvokeTask;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.RuntimeUtil;
import cn.moyada.dubbo.faker.core.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Main {

    @Autowired
    private FakerManager fakerManager;

    @Autowired
    private MethodHandleProxy methodHandleProxy;

    public String invoke(InvokerInfo invokerInfo) {
        invokerInfo.setPoolSize(RuntimeUtil.getActualSize(invokerInfo.getPoolSize()));

        MethodInvokeDO invokeInfo = fakerManager.getInvokeInfo(invokerInfo.getInvokeId());
        MethodProxy proxy = methodHandleProxy.getProxy(invokeInfo, invokerInfo.getPoolSize());

        Object[] values = JsonUtil.toArray(invokerInfo.getInvokeExpression(), Object[].class);
        Class<?>[] paramTypes = proxy.getParamTypes();
        if(null == values || paramTypes.length != values.length) {
            throw InitializeInvokerException.paramError;
        }

        // 生成调用报告序号
        String fakerId = UUIDUtil.getUUID();
        proxy.setFakerId(fakerId);
        proxy.setValues(values);

        InvokeTask invokeTask = new InvokeTask(proxy, invokerInfo).build();

        int qps = invokerInfo.getQps();
        int timeout = (3600 / qps) - (20 >= qps ? 0 : 50);
        // 发起调用
        if(timeout > 50) {
            invokeTask.start(timeout);
        }
        else {
            invokeTask.start();
        }

        invokeTask.shutdown();

        return "请求结果序号：" + fakerId;
    }
}
