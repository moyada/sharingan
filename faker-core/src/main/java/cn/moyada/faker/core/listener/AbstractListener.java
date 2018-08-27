package cn.moyada.faker.core.listener;


import cn.moyada.faker.common.model.queue.AbstractQueue;
import cn.moyada.faker.core.QuestInfo;
import cn.moyada.faker.core.handler.InvokeRecordHandler;
import cn.moyada.faker.core.handler.RecordHandler;
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
public abstract class AbstractListener<T> implements ListenerAction, InvokeCallback {

    @Autowired
    protected FakerManager fakerManager;

    protected RecordHandler recordHandler;

    protected final AbstractQueue<LogDO> queue;

    protected AbstractListener(AbstractQueue<LogDO> queue) {
        this.queue = queue;
    }

    @Override
    public void startListener(TaskEnvironment env) {
        QuestInfo invokerInfo = env.getQuestInfo();
        this.recordHandler = new InvokeRecordHandler(env.getFakerId(), invokerInfo.getInvokeId(),
                invokerInfo.isSaveResult(), invokerInfo.getResultParam());
    }

    @Override
    public void callback(Result result) {
        LogDO logDO = recordHandler.receive(result);
        queue.offer(logDO);
    }
}
