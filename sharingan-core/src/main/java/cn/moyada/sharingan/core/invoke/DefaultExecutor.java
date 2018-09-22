package cn.moyada.sharingan.core.invoke;

/**
 * 默认调用器
 * @author xueyikang
 * @since 1.0
 */
public class DefaultExecutor extends AbstractConcurrentExecutor implements JobExecutor {

    public DefaultExecutor(String fakerId, int poolSize, int questNum) {
        super(getThreadPool(fakerId, poolSize, questNum));
    }

    @Override
    public void run(Runnable task) {
        executor.execute(task);
    }
}
