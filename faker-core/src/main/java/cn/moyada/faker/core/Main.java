package cn.moyada.faker.core;

import cn.moyada.faker.common.model.MethodProxy;
import cn.moyada.faker.common.utils.JsonUtil;
import cn.moyada.faker.common.utils.RuntimeUtil;
import cn.moyada.faker.common.utils.UUIDUtil;
import cn.moyada.faker.core.proxy.MethodHandleProxy;
import cn.moyada.faker.core.task.InvokeTask;
import cn.moyada.faker.manager.FakerManager;
import cn.moyada.faker.manager.domain.MethodInvokeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Main {

    @Autowired
    private FakerManager fakerManager;

    @Autowired
    private MethodHandleProxy methodHandleProxy;

    public String invoke(QuestInfo questInfo) {
        questInfo.setPoolSize(RuntimeUtil.getActualSize(questInfo.getPoolSize()));

        MethodInvokeDO invokeInfo = fakerManager.getInvokeInfo(questInfo.getInvokeId());

        MethodProxy proxy = methodHandleProxy.getProxy(invokeInfo);

        Object[] values = JsonUtil.toArray(questInfo.getInvokeExpression(), Object[].class);
        Class<?>[] paramTypes = proxy.getParamTypes();
        if(null == values || paramTypes.length != values.length) {
            throw new IllegalStateException("param number error.");
        }

        // 生成调用报告序号
        String fakerId = UUIDUtil.getUUID();
        proxy.setFakerId(fakerId);
        proxy.setValues(values);

        InvokeTask invokeTask = new InvokeTask(proxy, questInfo);

        int qps = questInfo.getQps();
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
