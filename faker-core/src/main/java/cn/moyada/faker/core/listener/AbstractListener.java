package cn.moyada.faker.core.listener;


import cn.moyada.faker.core.common.QuestInfo;
import cn.moyada.faker.core.handler.InvokeRecordHandler;
import cn.moyada.faker.core.handler.RecordHandler;
import cn.moyada.faker.core.queue.AbstractQueue;
import cn.moyada.faker.core.task.TaskEnvironment;
import cn.moyada.faker.manager.FakerManager;
import cn.moyada.faker.manager.domain.LogDO;
import cn.moyada.faker.rpc.api.invoke.InvokeCallback;
import cn.moyada.faker.rpc.api.invoke.Result;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 监听器
 * @author xueyikang
 * @create 2018-03-18 17:12
 */
public abstract class AbstractListener implements ListenerAction, InvokeCallback {

    @Autowired
    protected FakerManager fakerManager;

    protected RecordHandler<LogDO> recordHandler;

    protected final AbstractQueue<LogDO> queue;

    protected AbstractListener(TaskEnvironment env, AbstractQueue<LogDO> queue) {
        QuestInfo invokerInfo = env.getQuestInfo();
        this.recordHandler = new InvokeRecordHandler(env.getFakerId(), invokerInfo.getInvokeId(),
                invokerInfo.isSaveResult(), invokerInfo.getResultParam());
        this.queue = queue;
    }

    @Override
    public void callback(Result result) {
        LogDO receive = recordHandler.receive(result);
        queue.offer(receive);
    }
}
