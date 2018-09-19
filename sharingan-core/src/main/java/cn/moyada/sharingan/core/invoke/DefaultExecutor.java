package cn.moyada.sharingan.core.invoke;

public class DefaultExecutor extends AbstractExecutor implements JobAction {

    public DefaultExecutor(String fakerId, int poolSize, int questNum) {
        super(getThreadPool(fakerId, poolSize, questNum));
    }

    @Override
    public void run(Runnable task) {
        executor.execute(task);
    }
}
