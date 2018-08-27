package cn.moyada.faker.core;

import cn.moyada.faker.common.exception.InitializeInvokerException;
import cn.moyada.faker.common.utils.RuntimeUtil;
import cn.moyada.faker.core.task.InvocationTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Main {

    @Autowired
    private InvocationTask invocationTask;

    public String invoke(QuestInfo questInfo) throws InitializeInvokerException {
        questInfo.setPoolSize(RuntimeUtil.getActualSize(questInfo.getPoolSize()));

        return invocationTask.runTask(questInfo);
    }
}
