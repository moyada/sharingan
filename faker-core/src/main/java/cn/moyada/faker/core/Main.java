package cn.moyada.faker.core;

import cn.moyada.faker.common.exception.InitializeInvokerException;
import cn.moyada.faker.common.utils.RuntimeUtil;
import cn.moyada.faker.core.common.QuestInfo;
import cn.moyada.faker.core.task.TaskActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Main {

    @Autowired
    private TaskActivity taskActivity;

    public String invoke(QuestInfo questInfo) throws InitializeInvokerException {
        questInfo.setPoolSize(RuntimeUtil.getActualSize(questInfo.getPoolSize()));

        return taskActivity.runTask(questInfo);
    }
}
