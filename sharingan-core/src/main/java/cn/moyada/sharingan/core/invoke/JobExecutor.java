package cn.moyada.sharingan.core.invoke;

/**
 * 任务调度器
 */
public interface JobExecutor {

    /**
     * 发起任务调用
     * @param task
     */
    void run(Runnable task);
}
