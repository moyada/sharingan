package cn.moyada.dubbo.faker.core.thread;

/**
 * @author xueyikang
 * @create 2018-03-29 10:53
 */
public class PriorityThread implements Runnable{

    private Runnable runnable;

    public PriorityThread(Runnable runnable) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run();
    }
}
