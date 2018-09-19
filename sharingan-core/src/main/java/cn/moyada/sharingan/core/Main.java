package cn.moyada.sharingan.core;

import cn.moyada.sharingan.common.exception.InitializeInvokerException;
import cn.moyada.sharingan.common.utils.RuntimeUtil;
import cn.moyada.sharingan.core.common.QuestInfo;
import cn.moyada.sharingan.core.task.TaskActivity;
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
