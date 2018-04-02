package cn.moyada.dubbo.faker.core.thread;

/**
 * 优先级线程
 * @author xueyikang
 * @create 2018-03-29 10:53
 */
public class PriorityThread extends Thread {

    public PriorityThread(Runnable target) {
        this(target, Thread.MAX_PRIORITY);
    }

    public PriorityThread(Runnable target, int priority) {
        super(target);
        this.setPriority(priority);
    }
}