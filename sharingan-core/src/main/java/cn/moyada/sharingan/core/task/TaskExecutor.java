package cn.moyada.sharingan.core.task;

import cn.moyada.sharingan.common.constant.TimeConstant;
import cn.moyada.sharingan.core.common.QuestInfo;
import cn.moyada.sharingan.core.invoke.JobExecutor;
import cn.moyada.sharingan.core.listener.ListenerAction;
import cn.moyada.sharingan.core.support.ArgsProviderContainer;
import cn.moyada.sharingan.rpc.api.invoke.AsyncInvoke;
import cn.moyada.sharingan.rpc.api.invoke.Invocation;

import java.util.concurrent.locks.LockSupport;

/**
 * 任务执行器
 */
public class TaskExecutor {

    private final AsyncInvoke invoke;
    private final JobExecutor jobExecutor;

    // 参数提供器
    private final ArgsProviderContainer container;

    // 结果监听器
    private final ListenerAction listener;

    public TaskExecutor(AsyncInvoke invoke, ListenerAction listener, JobExecutor jobExecutor,
                        ArgsProviderContainer container) {
        // 注册监听者
        invoke.register(listener);

        this.invoke = invoke;
        this.container = container;
        this.listener = listener;
        this.jobExecutor = jobExecutor;
    }

    /**
     * 执行请求任务
     * @param questInfo
     */
    public void execute(QuestInfo questInfo) {
        int questNum = questInfo.getQuestNum();
        int qps = questInfo.getQps();
        int timeout = (3600 / qps) - (20 >= qps ? 0 : 50);

        listener.process();

        // 发起调用
        if (timeout > 50) {
            runWithTimeout(questNum, timeout);
        } else {
            run(questNum);
        }

        listener.waitFinish();
    }

    private void run(int questNum) {
        for (int index = 0; index < questNum; index++) {
            Object[] param = container.getArgs();

            Invocation invocation = new Invocation();
            invocation.setArgsValue(param);

            jobExecutor.run(() -> invoke.call(invocation));
        }
    }

    private void runWithTimeout(int questNum, int timeout) {
        for (int index = 0; index < questNum; index++) {
            Object[] param = container.getArgs();

            Invocation invocation = new Invocation();
            invocation.setArgsValue(param);

            jobExecutor.run(() -> invoke.call(invocation));

            LockSupport.parkNanos(timeout * TimeConstant.NANO_PER_MILLIS);
        }
    }
}
