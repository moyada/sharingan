package io.moyada.sharingan.domain.task;


import io.moyada.sharingan.domain.expression.ParamProvider;
import io.moyada.sharingan.infrastructure.constant.TimeConstant;
import io.moyada.sharingan.infrastructure.invoke.AsyncInvoke;
import io.moyada.sharingan.infrastructure.invoke.Invocation;
import io.moyada.sharingan.infrastructure.util.RuntimeUtil;
import io.moyada.sharingan.infrastructure.util.TimeUtil;

import java.util.concurrent.locks.LockSupport;

/**
 * 任务执行器
 */
public class TaskProcessor {

    private final AsyncInvoke invoke;

    // 参数提供器
    private final ParamProvider container;

    // 结果监听器
    private final ListenerAction listener;

    private final TaskExecutor taskExecutor;

    public TaskProcessor(AsyncInvoke invoke, ListenerAction listener, TaskExecutor taskExecutor,
                         ParamProvider container) {
        // 注册监听者
        invoke.register(listener);

        this.invoke = invoke;
        this.container = container;
        this.listener = listener;
        this.taskExecutor = taskExecutor;
    }

    /**
     * 执行请求任务
     * @param quest
     */
    public ReportData start(int quest, int qps) {
        int timeout = 3600 / qps;

        RuntimeUtil.gc(100L);
        TimeUtil.doTimekeeping();

        listener.process();

        // 发起调用
        if (timeout > 50) {
            runWithTimeout(quest, timeout);
        } else {
            run(quest);
        }

        ReportData reportData = listener.waitFinish();

        taskExecutor.shutdown();
        TimeUtil.stopTimekeeping();
        return reportData;
    }

    private void run(int questNum) {
        for (int index = 0; index < questNum; index++) {
            taskExecutor.execute(() -> invoke.call(nextParam()));
        }
    }

    private void runWithTimeout(int questNum, int timeout) {
        for (int index = 0; index < questNum; index++) {
            taskExecutor.execute(() -> invoke.call(nextParam()));
            LockSupport.parkNanos(timeout * TimeConstant.NANO_PER_MILLIS);
        }
    }

    private Invocation nextParam() {
        return new Invocation(container.getNext());
    }
}
