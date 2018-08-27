package cn.moyada.faker.core.invoke;

import cn.moyada.faker.core.common.QuestInfo;

public class DefaultExecutor extends AbstractExecutor implements JobAction {

    public DefaultExecutor(QuestInfo questInfo, String fakerId) {
        super(getThreadPool(questInfo, fakerId));
    }

    @Override
    public void run(Runnable task) {
        executor.execute(task);
    }
}
