package cn.moyada.faker.core.task;

import cn.moyada.faker.common.constant.TimeConstant;
import cn.moyada.faker.core.common.QuestInfo;
import cn.moyada.faker.core.invoke.JobAction;
import cn.moyada.faker.core.listener.ListenerAction;
import cn.moyada.faker.core.provider.ParamProvider;
import cn.moyada.faker.rpc.api.invoke.AsyncInvoke;
import cn.moyada.faker.rpc.api.invoke.Invocation;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

public class AbstractTaskActivity {

    private final Queue<Invocation> paramQueue;

    private final AsyncInvoke invoke;
    private final JobAction jobAction;

    // 参数提供器
    private final ParamProvider paramProvider;

    // 结果监听器
    private final ListenerAction listener;

    public AbstractTaskActivity(AsyncInvoke invoke, ListenerAction listener,
                                ParamProvider paramProvider, JobAction jobAction) {
        invoke.register(listener);
        this.invoke = invoke;
        this.paramProvider = paramProvider;
        this.listener = listener;
        this.jobAction = jobAction;
        this.paramQueue = new ConcurrentLinkedQueue<>();
    }

    public void start(QuestInfo questInfo) {
        int questNum = questInfo.getQuestNum();
        int qps = questInfo.getQps();
        int timeout = (3600 / qps) - (20 >= qps ? 0 : 50);

        genParam(questNum);

        listener.startListener();

        // 发起调用
        if (timeout > 50) {
            run(questNum, timeout);
        } else {
            run(questNum);
        }

        listener.waitFinish();
    }

    private void genParam(int num) {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < num; i++) {
                Object[] param = paramProvider.fetchNextParam();
                Invocation invocation = new Invocation();
                invocation.setArgsValue(param);

                paramQueue.offer(invocation);
            }
        });
        thread.setName("param generator");
        thread.setPriority(Thread.NORM_PRIORITY + 1);
        thread.start();
    }

    private void run(int questNum) {
        for (int index = 0; index < questNum; index++) {
            Invocation invocation = paramQueue.poll();
            if (null == invocation) {
                Thread.yield();
            }
            execute(invocation);
        }
    }

    private void run(int questNum, int timeout) {
        for (int index = 0; index < questNum; index++) {
            Invocation invocation = paramQueue.poll();
            if (null == invocation) {
                Thread.yield();
            }
            execute(invocation);

            LockSupport.parkNanos(timeout * TimeConstant.NANO_PER_MILLIS);
        }
    }

    protected void execute(Invocation invocation) {
        jobAction.run(() -> invoke.call(invocation));
    }
}
