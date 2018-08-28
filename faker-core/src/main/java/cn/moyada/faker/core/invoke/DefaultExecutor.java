package cn.moyada.faker.core.invoke;

import cn.moyada.faker.core.common.QuestInfo;

public class DefaultExecutor extends AbstractExecutor implements JobAction {

    public DefaultExecutor(String fakerId, QuestInfo questInfo) {
        super(getThreadPool(fakerId, questInfo));
    }

    @Override
    public void run(Runnable task) {
        executor.execute(task);
    }
}
